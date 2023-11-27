package com.test.products.repository;

import com.test.products.exception.DBException;
import com.test.products.model.Product;
import com.test.products.model.entity.Table;
import com.test.products.model.payload.AddProductsRequest;
import com.test.products.model.payload.ProductsResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

@Log4j2
@AllArgsConstructor
@Component
public class ProductDaoImpl implements ProductDao {

    private static final String PRODUCTS_WITH_DIFFERENT_COLUMN_KEYS = "Products have different keys and can not be persisted to the table";
    private static final String SELECT_FROM = "SELECT * FROM ";
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public int saveProducts(AddProductsRequest request) {

        if (!validateProductRecordsHaveEqualKeys(request.getRecords())) {
            log.error(PRODUCTS_WITH_DIFFERENT_COLUMN_KEYS);
            throw new DBException(PRODUCTS_WITH_DIFFERENT_COLUMN_KEYS);
        }
        createTable(request);
        return insertProducts(request);
    }

    @Override
    public ProductsResponse getAllProducts() {

        Map<String, List<? extends Product>> products = new HashMap<>();

        Query getTableNamesQuery = entityManager.createNativeQuery(SELECT_FROM + "tables", Table.class);
        List<Table> tables = (List<Table>) getTableNamesQuery.getResultList();

        for (Table table : tables) {
            Query getProductsQuery = entityManager.createNativeQuery(SELECT_FROM + table.getName(), Product.class);
            List<Product> tableProducts = (List<Product>) getProductsQuery.getResultList();
            products.put(table.getName(), tableProducts);
        }
        return ProductsResponse.builder()
                .productsTables(products)
                .build();
    }

    private void createTable(AddProductsRequest request) {
        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        createTableQuery.append(request.getTable());
        createTableQuery.append(" (id INT AUTO_INCREMENT PRIMARY KEY");

        for (String key : getColumnNames(request)) {
            createTableQuery.append(", ").append(key).append(" VARCHAR(255)");
        }
        createTableQuery.append(")");
        entityManager.createNativeQuery(createTableQuery.toString()).executeUpdate();

        saveTableName(request.getTable());
    }

    private void saveTableName(String tableName) {
        StringBuilder saveTableNameQuery = new StringBuilder("INSERT INTO tables (name) VALUES ('");
        saveTableNameQuery.append(tableName);
        saveTableNameQuery.append("')");
        entityManager.createNativeQuery(saveTableNameQuery.toString()).executeUpdate();
    }

    private int insertProducts(AddProductsRequest request) {
        StringBuilder insertProductsQuery = new StringBuilder("INSERT INTO ");
        insertProductsQuery.append(request.getTable());
        insertProductsQuery.append(" (");
        addColumnsForInsertQuery(insertProductsQuery, getColumnNames(request));
        insertProductsQuery.append(") VALUES (");
        return executeInsertProducts(insertProductsQuery.toString(), request.getRecords());
    }


    private void addColumnsForInsertQuery(StringBuilder insertProductsQuery, Set<String> columnNames) {
        int i = 0;
        for (String key : columnNames) {
            if (i != 0) {
                insertProductsQuery.append(", ");
            }
            insertProductsQuery.append(key);
            i++;
        }
    }

    private Set<String> getColumnNames(AddProductsRequest request) {
        return request.getRecords().get(0).keySet();
    }

    private int executeInsertProducts(String insertProductsQuery, List<Map<String, String>> records) {
        int result = 0;
        for (Map<String, String> productRecord : records) {
            StringBuilder insertProductValuesQuery = new StringBuilder(insertProductsQuery);
            int i = 0;
            for (String value : productRecord.values()) {
                if (i != 0) {
                    insertProductValuesQuery.append(", ");
                }
                insertProductValuesQuery.append("'").append(value).append("'");
                i++;
            }
            insertProductValuesQuery.append(")");
            result += entityManager.createNativeQuery(insertProductValuesQuery.toString()).executeUpdate();
        }
        return result;
    }

    private boolean validateProductRecordsHaveEqualKeys(List<Map<String, String>> records) {
        return IntStream.range(0, records.size())
                .allMatch(i -> IntStream.range(i + 1, records.size())
                        .allMatch(j -> records.get(i).keySet().equals(records.get(j).keySet())));
    }
}

package com.test.products.repository;

import com.test.products.exception.DBException;
import com.test.products.model.payload.AddProductsRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

@Log4j2
@AllArgsConstructor
@Component
public class ProductDaoImpl implements ProductDao {

    private static final String PRODUCTS_WITH_DIFFERENT_COLUMN_KEYS = "Products have different keys and can not be persisted to the table";

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

    private void createTable(AddProductsRequest request) {
        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS ");
        createTableQuery.append(request.getTable());
        createTableQuery.append(" (id INT AUTO_INCREMENT PRIMARY KEY");

        for (String key : getColumnNames(request)) {
            createTableQuery.append(", ").append(key).append(" VARCHAR(255)");
        }
        createTableQuery.append(")");
        entityManager.createNativeQuery(createTableQuery.toString()).executeUpdate();
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

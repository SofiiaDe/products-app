package com.test.products.repository;

import com.test.products.model.payload.AddProductsResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.extern.log4j.Log4j2;
import org.hibernate.Session;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Log4j2
public class ProductDaoImpl implements ProductDao {

    private final EntityManagerFactory entityManagerFactory;
    private final TransactionTemplate transactionTemplate;

    public ProductDaoImpl(EntityManagerFactory entityManagerFactory, PlatformTransactionManager transactionManager) {
        this.entityManagerFactory = entityManagerFactory;
        this.transactionTemplate = new TransactionTemplate(transactionManager);
    }

    @Override
    @Transactional
    public AddProductsResponse saveProducts(String tableName, String columnsDefinition, String columnsNames, String values) {
        return execute(e -> {
            Query query = e.createNativeQuery(
                    "CREATE TABLE IF NOT EXISTS :tableName (id BIGINT AUTO_INCREMENT PRIMARY KEY :columnsDefinition);");
            query.setParameter("tableName", tableName);
            query.setParameter("columnsDefinition", columnsDefinition);
            query.executeUpdate();

            Query nativeQuery = e.createNativeQuery("INSERT INTO :tableName (:columnsNames) VALUES (:values)");
            nativeQuery.setParameter("tableName", tableName);
            nativeQuery.setParameter("columnsNames", columnsNames);
            nativeQuery.setParameter("values", values);
//            Session session = entityManager.unwrap(HibernateEntityManager.class).getSession();
//            org.hibernate.Query nativeQuery = session.createNativeQuery("SELECT * FROM _tmp_siteoutage_summary");
            List resultList = nativeQuery.getResultList();

            return AddProductsResponse.builder()
                    .tableName(tableName)
                    .productsCount(resultList.size())
                    .build();
        });
    }

    private <R> R execute(Function<EntityManager, R> function) {
        EntityManager entityManager = null;
        try {
            entityManager = entityManagerFactory.createEntityManager();
            return function.apply(entityManager);
        } finally {
            try {
                if (entityManager != null) {
                    entityManager.close();
                }

            } catch (Exception e) {
                log.warn("Error closing entity manager", e);
            }
        }
    }

}

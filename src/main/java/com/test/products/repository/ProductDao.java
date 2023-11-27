package com.test.products.repository;

import com.test.products.model.payload.AddProductsResponse;

public interface ProductDao {

    AddProductsResponse saveProducts(String tableName, String columnsDefinition, String columnsNames, String values);
}

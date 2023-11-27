package com.test.products.repository;

import com.test.products.model.payload.AddProductsRequest;

public interface ProductDao {

    int saveProducts(AddProductsRequest request);
}

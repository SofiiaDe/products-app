package com.test.products.repository;

import com.test.products.model.payload.AddProductsRequest;
import com.test.products.model.payload.ProductsResponse;

public interface ProductDao {

    int saveProducts(AddProductsRequest request);

    ProductsResponse getAllProducts();
}

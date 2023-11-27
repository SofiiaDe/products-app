package com.test.products.service;

import com.test.products.model.payload.AddProductsRequest;
import com.test.products.model.payload.ProductsResponse;

public interface ProductService {

    int addProducts(AddProductsRequest request);

    ProductsResponse getAllProducts();

}
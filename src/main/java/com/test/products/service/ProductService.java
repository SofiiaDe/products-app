package com.test.products.service;

import com.test.products.model.payload.AddProductsRequest;
import com.test.products.model.payload.AddProductsResponse;

public interface ProductService {

    AddProductsResponse addProducts(AddProductsRequest request);

}

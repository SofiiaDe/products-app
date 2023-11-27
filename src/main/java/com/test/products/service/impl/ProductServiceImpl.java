package com.test.products.service.impl;

import com.test.products.exception.DBException;
import com.test.products.model.payload.AddProductsRequest;
import com.test.products.model.payload.AddProductsResponse;
import com.test.products.repository.ProductDao;
import com.test.products.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private ProductDao productDao;

    @Override
    @Transactional
    public AddProductsResponse addProducts(AddProductsRequest request) {
        AddProductsResponse response;
        try {
            response = productDao.saveProducts(request.getTable(), getColumnsDefinitionAsString(request),
                    getColumnsNamesAsString(request), getProductValues(request));
        } catch (DBException exception) {
            throw new DBException("Can't save products");
        }
        return response;
    }

    private String getColumnsDefinitionAsString(AddProductsRequest request) {
        StringBuilder columns = new StringBuilder();
        for (String key : request.getRecords().get(0).keySet()) {
            columns
                    .append(", ")
                    .append(key)
                    .append(" VARCHAR(255)"); // differentiate db type
        }
        columns.append(")");
        return columns.toString();
    }

    private String getColumnsNamesAsString(AddProductsRequest request) {
        StringBuilder columns = new StringBuilder();
        for (String key : request.getRecords().get(0).keySet()) {
            columns
                    .append(", ")
                    .append(key);
        }
        columns.append(")");
        return columns.toString();
    }

    private String getProductValues(AddProductsRequest request) {
        StringBuilder columns = new StringBuilder();
        for (Object value : request.getRecords().get(0).values()) {
            columns
                    .append(", ")
                    .append(value);
        }
        columns.append(")");
        return columns.toString();
    }

}
package com.test.products.service.impl;

import com.test.products.exception.DBException;
import com.test.products.model.payload.AddProductsRequest;
import com.test.products.repository.ProductDao;
import com.test.products.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@Service
@AllArgsConstructor
public class ProductServiceImpl implements ProductService {

    private static final String CAN_NOT_SAVE_PRODUCTS = "Can't save products";

    private ProductDao productDao;

    @Override
    @Transactional
    public int addProducts(AddProductsRequest request) {
        int savedRecordsCount;
        try {
            savedRecordsCount = productDao.saveProducts(request);
        } catch (DBException exception) {
            log.error(CAN_NOT_SAVE_PRODUCTS);
            throw new DBException(CAN_NOT_SAVE_PRODUCTS);
        }
        return savedRecordsCount;
    }

}
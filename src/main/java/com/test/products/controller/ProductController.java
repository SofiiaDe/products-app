package com.test.products.controller;

import com.test.products.model.payload.AddProductsRequest;
import com.test.products.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController extends BaseController {

    private final ProductService productService;

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> addProducts(@Valid @RequestBody AddProductsRequest request, BindingResult result) {
        if (result.hasErrors()) {
            return handleValidationErrors(result);
        }
        return ResponseEntity.status(HttpStatus.OK)
                .body(productService.addProducts(request));
    }
}

package com.test.products.model.payload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddProductsRequest {

    private String table;
    // validate equal keys in each entity
    private List<Map<String, Object>> records;
}

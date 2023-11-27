package com.test.products.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonProperty("entry_date")
    private String entryDate;

    @JsonProperty("itemcode")
    private String itemCode;

    @JsonProperty("itemname")
    private String itemName;

    @JsonProperty("itemquantity")
    private Integer itemQuantity;

    private String upc;
    private String ean;
    private String sku;
    private String isbn;
    private String mpc;
    private String sStatus;
}
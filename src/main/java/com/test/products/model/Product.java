package com.test.products.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    private Integer id;

    @JsonProperty("entry_date")
    private String entryDate;

    @JsonProperty("itemcode")
    private String itemCode;

    @JsonProperty("itemname")
    private String itemName;

    @JsonProperty("itemquantity")
    private String itemQuantity;

    private String upc;
    private String ean;
    private String sku;
    private String isbn;
    private String mpc;
    private String sStatus;

}

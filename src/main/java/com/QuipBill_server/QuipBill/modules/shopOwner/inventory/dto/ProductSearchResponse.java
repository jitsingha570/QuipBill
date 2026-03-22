package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSearchResponse {

    private Long productId;
    private String productName;
    private Double price;
    private Double gstPercent;
    private Boolean gstEnabled;
    private Integer quantity;
}
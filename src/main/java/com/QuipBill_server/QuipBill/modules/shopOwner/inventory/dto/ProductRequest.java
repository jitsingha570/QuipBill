package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequest {

    private Long shopId;

    private String productName;

    private String barcode;

    private Double price;

    private Double gstPercent;

    private Boolean gstEnabled;

    private Integer quantity;

}

package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponse {

    private Long productId;

    private Long shopId;

    private String productName;

    private String barcode;

    private Integer quantity;

    private Double price;

}

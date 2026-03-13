package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarcodeGenerateRequest {
    
    private Long shopId;

    private String productName;

    private Double price;

}

package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarcodeGenerateResponse {

    private Long productId;

    private String barcode;

    private String labelUrl;

}
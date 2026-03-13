package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BarcodeLookupResponse {

    private Boolean exists;

    private String barcode;

    private ProductResponse product;

}
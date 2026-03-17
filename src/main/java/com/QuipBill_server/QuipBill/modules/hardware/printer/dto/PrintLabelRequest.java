package com.QuipBill_server.QuipBill.modules.hardware.printer.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrintLabelRequest {

    private String shopName;

    private String productName;

    private Double price;

    private String barcode;

    

}
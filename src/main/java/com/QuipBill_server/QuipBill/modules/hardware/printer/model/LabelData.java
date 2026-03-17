package com.QuipBill_server.QuipBill.modules.hardware.printer.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabelData {

    private String shopName;

    private String productName;

    private Double price;

    private String barcode;

}
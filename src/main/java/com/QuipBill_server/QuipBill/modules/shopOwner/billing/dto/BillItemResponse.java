package com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillItemResponse {

    private Long productId;

    private String productName;

    private Double price;

    private Integer quantity;

    private Double discountPercent;

    private Boolean gstEnabled;

    private Double gstPercent;

    private Double gstAmount;

    private Double finalAmount;
}

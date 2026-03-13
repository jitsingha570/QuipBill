package com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillResponse {

    private Long billId;

    private String billNumber;

    private Long shopId;

    private String customerName;

    private String billingMode;

    private Double subtotal;

    private Double finalDiscount;

    private Double roundOff;

    private Double grandTotal;

    private Double finalAmount;

    private LocalDateTime createdAt;

    private List<BillItemResponse> items;

    private String message;
}

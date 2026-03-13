package com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrintableBillResponse {

    private Long billId;

    private String shopName;

    private String shopAddress;

    private String shopPhone;

    private String customerName;

    private LocalDateTime createdAt;

    private List<PrintableItem> items;

    private Double subtotal;

    private Double finalDiscount;

    private Double roundOff;

    private Double grandTotal;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PrintableItem {

        private String productName;

        private Double price;

        private Integer quantity;

        private Double discountPercent;

        private Double finalAmount;
    }
}
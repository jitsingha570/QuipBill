package com.QuipBill_server.QuipBill.modules.hardware.printer.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReceiptData {

    private Long billId;

    private String shopName;

    private String shopAddress;

    private String shopPhone;

    private String customerName;

    private String paymentMethod;

    private LocalDateTime createdAt;

    private List<ReceiptItem> items;

    private Double subtotal;

    private Double gstPercent;

    private Double gstAmount;

    private Boolean gstEnabled;

    private Double finalDiscount;

    private Double roundOff;

    private Double grandTotal;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ReceiptItem {

        private String itemName;

        private Double price;

        private Integer quantity;

        private Double discountPercent;

        private Double finalAmount;

    }

}
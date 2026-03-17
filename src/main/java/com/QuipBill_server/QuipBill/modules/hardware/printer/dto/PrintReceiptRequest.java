package com.QuipBill_server.QuipBill.modules.hardware.printer.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrintReceiptRequest {

    private Long billId;

    private String shopName;

    private String shopAddress;

    private String shopPhone;

    private String customerName;

    private String paymentMethod;

    private LocalDateTime createdAt;

    private List<ReceiptItem> items;

    private Double subtotal;

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

        private Double totalPrice;

        private Double finalAmount;

    }

}
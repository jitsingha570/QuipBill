package com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto;

import lombok.*; //lombok is a java library that helps to reduce boilerplate code by generating getters, setters, constructors, builders, etc. at compile time using annotations.


import jakarta.validation.constraints.*; //validation annotations for validating the request body fields like @NotNull, @Min, @NotEmpty, etc.


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillItemRequest {
    
    private Long productId;
    private String productName;
    private String barcode;

    @NotNull(message = "Price is required")
    private Double price;
    @NotNull(message = "Quantity is required")
    private Integer quantity;
    private Double discountPercent;

    // GST Fields
    private Boolean gstEnabled;
    private Double gstPercent;

    // 🔥 Smart Setter: Keep GST fields consistent
    public void setGstPercent(Double gstPercent) {
        this.gstPercent = gstPercent;

        if (gstPercent == null || gstPercent <= 0) {
            this.gstEnabled = false;
            this.gstPercent = 0.0;
        } else {
            this.gstEnabled = true;
        }
    }

    // 🔥 Extra Safety: If gstEnabled set manually
    public void setGstEnabled(Boolean gstEnabled) {
        this.gstEnabled = gstEnabled;

        if (Boolean.FALSE.equals(gstEnabled)) {
            this.gstPercent = 0.0;
        }
    }
}

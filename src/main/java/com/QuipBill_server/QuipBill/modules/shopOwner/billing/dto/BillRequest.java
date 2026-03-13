package com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto;

import com.QuipBill_server.QuipBill.modules.shopOwner.billing.enums.BillingMode;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillRequest {

    // Optional field
    private String customerName;

    @NotNull(message = "Billing mode is required")
    private BillingMode billingMode;

    // Optional
    @Min(value = 0, message = "Discount cannot be negative")
    private Double finalDiscount;

    @NotEmpty(message = "Bill must contain at least one item")
    @Valid
    private List<BillItemRequest> items;
}

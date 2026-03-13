package com.QuipBill_server.QuipBill.modules.shopOwner.billing.utils;

import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillItemRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BillingCalculator {

    // Calculate final amount of a single item (with Discount + Optional GST)
    public double calculateItemFinalAmount(BillItemRequest item) {

        // Step 1: Base total
        double itemTotal = item.getPrice() * item.getQuantity();

        // Step 2: Discount
        double discountAmount =
                (itemTotal * item.getDiscountPercent()) / 100.0;

        double afterDiscount = itemTotal - discountAmount;

        // Step 3: GST (Only if enabled)
        if (Boolean.TRUE.equals(item.getGstEnabled()) && item.getGstPercent() != null) {

            double gstAmount =
                    (afterDiscount * item.getGstPercent()) / 100.0;

            return afterDiscount + gstAmount;
        }

        return afterDiscount;
    }

    // Calculate subtotal (sum of all items)
    public double calculateSubtotal(List<BillItemRequest> items) {

        return items.stream()
                .mapToDouble(this::calculateItemFinalAmount)
                .sum();
    }

    // Calculate round off
    public double calculateRoundOff(double amount) {

        double rounded = Math.round(amount);
        return rounded - amount;
    }

    // Calculate final grand total
    public double calculateGrandTotal(double subtotal, double finalDiscount) {

        double afterDiscount = subtotal - finalDiscount;

        return Math.round(afterDiscount);
    }
}
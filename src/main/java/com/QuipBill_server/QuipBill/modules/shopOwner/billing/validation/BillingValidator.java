package com.QuipBill_server.QuipBill.modules.shopOwner.billing.validation;


import com.QuipBill_server.QuipBill.common.exception.BadRequestException;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillItemRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillRequest;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class BillingValidator {

    private static final Set<Double> ALLOWED_GST_SLABS = Set.of(5.0, 12.0, 18.0);

    public void validateBillRequest(BillRequest request) {

        if (request == null) {
            throw new BadRequestException("Bill request cannot be null.");
        }

        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BadRequestException("Bill must contain at least one item.");
        }

        if (request.getFinalDiscount() == null) {
            request.setFinalDiscount(0.0);
        }

        if (request.getFinalDiscount() < 0) {
            throw new BadRequestException("Final discount cannot be negative.");
        }

        for (BillItemRequest item : request.getItems()) {
            validateItem(item);
        }
    }

    private void validateItem(BillItemRequest item) {

        if (item == null) {
            throw new BadRequestException("Bill item cannot be null.");
        }

        // Price validation
        if (item.getPrice() == null || item.getPrice() <= 0) {
            throw new BadRequestException("Item price must be greater than 0.");
        }

        // Quantity validation
        if (item.getQuantity() == null || item.getQuantity() <= 0) {
            throw new BadRequestException("Item quantity must be greater than 0.");
        }

        // Discount default
        if (item.getDiscountPercent() == null) {
            item.setDiscountPercent(0.0);
        }

        if (item.getDiscountPercent() < 0) {
            throw new BadRequestException("Item discount cannot be negative.");
        }

        if (item.getDiscountPercent() > 100) {
            throw new BadRequestException("Item discount cannot exceed 100%.");
        }

        // ===============================
        // GST VALIDATION
        // ===============================

        if (Boolean.TRUE.equals(item.getGstEnabled())) {

            if (item.getGstPercent() == null) {
                throw new BadRequestException("GST percent is required when GST is enabled.");
            }

            if (!ALLOWED_GST_SLABS.contains(item.getGstPercent())) {
                throw new BadRequestException(
                        "Invalid GST slab. Allowed values: 5%, 12%, 18%."
                );
            }

        } else {
            item.setGstPercent(0.0);
            item.setGstEnabled(false);
        }
    }
}
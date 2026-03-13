package com.QuipBill_server.QuipBill.modules.shopOwner.billing.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BillingMode {
    CASH,
    UPI;

    @JsonCreator
    public static BillingMode fromString(String value) {
        if (value == null) {
            return null;
        }
        return BillingMode.valueOf(value.trim().toUpperCase());
    }

    @JsonValue
    public String toJson() {
        return name();
    }
}

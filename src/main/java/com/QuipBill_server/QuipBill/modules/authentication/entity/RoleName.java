package com.QuipBill_server.QuipBill.modules.authentication.entity;

public enum RoleName {

    ROLE_ADMIN("Admin"),
    ROLE_SALESMAN("Salesman"),
    ROLE_SHOP("Shop");

    private final String displayName;

    RoleName(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
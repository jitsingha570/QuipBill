package com.QuipBill_server.QuipBill.modules.authentication.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public class ShopProfileUpdateRequest {

    @JsonAlias({"mobile_number"})
    private String mobileNumber;
    @JsonAlias({"dashboard_pin"})
    private String dashboardPin;
    private String shopName;
    @JsonAlias({"shop_owner_name"})
    private String shopOwnerName;
    private String address;
    private String pincode;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDashboardPin() {
        return dashboardPin;
    }

    public void setDashboardPin(String dashboardPin) {
        this.dashboardPin = dashboardPin;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}

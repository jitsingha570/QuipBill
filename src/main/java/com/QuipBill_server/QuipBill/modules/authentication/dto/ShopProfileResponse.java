package com.QuipBill_server.QuipBill.modules.authentication.dto;

public class ShopProfileResponse {

    private Long shopId;
    private String email;
    private String mobileNumber;
    private String shopName;
    private String shopOwnerName;
    private String address;
    private String pincode;

    public ShopProfileResponse(Long shopId,
                               String email,
                               String mobileNumber,
                               String shopName,
                               String shopOwnerName,
                               String address,
                               String pincode) {
        this.shopId = shopId;
        this.email = email;
        this.mobileNumber = mobileNumber;
        this.shopName = shopName;
        this.shopOwnerName = shopOwnerName;
        this.address = address;
        this.pincode = pincode;
    }

    public Long getShopId() {
        return shopId;
    }

    public String getEmail() {
        return email;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getShopName() {
        return shopName;
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public String getAddress() {
        return address;
    }

    public String getPincode() {
        return pincode;
    }
}

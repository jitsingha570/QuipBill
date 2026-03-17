package com.QuipBill_server.QuipBill.modules.authentication.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class RegisterRequest {

    //private String username;
    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Mobile number is required")
    @Size(min = 10, max = 10, message = "Mobile number must be 10 digits")
    @JsonAlias({"mobile_number"})
    private String mobileNumber;
    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;
    @NotBlank(message = "Dashboard PIN is required")
    @Size(min = 6, max = 6, message = "Dashboard PIN must be 6 digits")
    @JsonAlias({"dashboard_pin"})
    private String dashboardPin;


    @NotBlank(message = "Shop name is required")
    private String shopName;
    @NotBlank(message = "Shop owner name is required")
    @JsonAlias({"shop_owner_name"})
    private String shopOwnerName;
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "Pincode is required")
    @Size(min = 6, max = 6, message = "Pincode must be 6 digits")

    private String pincode;
    /*private Double latitude;
    private Double longitude;*/

    /*public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }*/

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getShopOwnerName(){
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName){
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

     public void setPincode( String pincode) {
             this.pincode = pincode;    
    }

    /*public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }*/
}

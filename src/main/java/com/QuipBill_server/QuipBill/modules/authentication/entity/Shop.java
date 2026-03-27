package com.QuipBill_server.QuipBill.modules.authentication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
    name = "users",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "email") // ✅ removed username constraint (not used)
    }
)
public class Shop {

    // ================= PRIMARY KEY =================
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ AUTO INCREMENT FIX
    private Long id;

    // ================= BASIC LOGIN =================

    @Column(nullable = false, length = 150)
    private String email;

    @Column(name = "mobile_number", length = 15)
    private String mobileNumber;

    @JsonIgnore
    @Column(nullable = false)
    private String password;

    @Column(name = "dashboard_pin", length = 10)
    private String dashboardPin;

    // ================= SHOP DETAILS =================

    @Column(name = "shop_name", nullable = false)
    private String shopName;

    @Column(name = "shop_owner_name", length = 100)
    private String shopOwnerName;

    @Column(length = 500)
    private String address;

    @Column(name = "pincode", length = 10)
    private String pincode;

    // ================= OTP =================

    @Column(length = 10)
    private String otp;

    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry;

    @Column(name = "is_verified")
    private Boolean verified = false;

    // ================= SYSTEM =================

    @Column(name = "is_active")
    private Boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // ================= ROLES =================

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),     // FK → users.id
        inverseJoinColumns = @JoinColumn(name = "role_id") // FK → roles.id
    )
    private Set<Role> roles = new HashSet<>();

    // ================= CONSTRUCTOR =================

    public Shop() {}

    // ================= LIFECYCLE =================

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ================= RELATION HELPERS =================

    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    // ================= GETTERS & SETTERS =================

    public Long getId() { return id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDashboardPin() { return dashboardPin; }
    public void setDashboardPin(String dashboardPin) { this.dashboardPin = dashboardPin; }

    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }

    public String getShopOwnerName() { return shopOwnerName; }
    public void setShopOwnerName(String shopOwnerName) { this.shopOwnerName = shopOwnerName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPincode() { return pincode; }
    public void setPincode(String pincode) { this.pincode = pincode; }

    public String getOtp() { return otp; }
    public void setOtp(String otp) { this.otp = otp; }

    public LocalDateTime getOtpExpiry() { return otpExpiry; }
    public void setOtpExpiry(LocalDateTime otpExpiry) { this.otpExpiry = otpExpiry; }

    public Boolean getVerified() { return verified; }
    public void setVerified(Boolean verified) { this.verified = verified; }

    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public Set<Role> getRoles() { return roles; }
}
package com.QuipBill_server.QuipBill.modules.authentication.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;//line decode 
import jakarta.persistence.*; //

import java.time.LocalDateTime;
import java.util.HashSet; //hashset use to avoid duplicate role , it is a collection of java 
import java.util.Set;  //set use to  

@Entity   //
@Table(name = "users", 
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
        // create a class which 
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ================= BASIC LOGIN =================

    //@Column(nullable = false, length = 100)
    //private String username;

    @Column(nullable = false, length = 150)
    private String email;

     @Column(name = "mobile_number", length = 15)
    private String mobileNumber;

    @JsonIgnore   // 🔥 Never expose password in API
    @Column(nullable = false)
    private String password;

    //pin for finally unlock the dashboard for shop owner
    @Column(name = "dashboard_pin", length = 10)
    private String dashboardPin;

    // ================= SHOP DETAILS =================

    @Column(name = "shop_name", nullable = false)
    private String shopName;

    @Column(name = "shopOwner_name", length = 100)
    private String shopOwnerName;

    @Column(length = 500)
    private String address;
    
    //pincode 

    @Column(name = "pincode", length = 10)
    private String pincode;
    //private Double latitude;
    //private Double longitude;

    // ================= OTP FIELDS =================

    @Column(length = 10)
    private String otp;

    private LocalDateTime otpExpiry;

    @Column(name = "is_verified")
    private Boolean verified = false;

    // ================= SYSTEM FIELDS =================

    @Column(name = "is_active") //its for admin to block the shop if they are doing any wrong activity
    private Boolean active = true;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
     
    
    // ================= ROLES =================

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    // ================= CONSTRUCTORS =================

    public Shop() {}

    // ================= LIFECYCLE =================

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ================= UTILITY METHODS =================

    public void addRole(Role role) {
        this.roles.add(role);
        role.getUsers().add(this);   // 🔥 Keep both sides synced
    }

    public void removeRole(Role role) {
        this.roles.remove(role);
        role.getUsers().remove(this);
    }

    // ================= GETTERS & SETTERS =================

    public Long getId() {
        return id;
    }

    /*public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
*/
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
   /*  public Double getLatitude() {
        return latitude;
    }*/

    /*public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }*/

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public LocalDateTime getOtpExpiry() {
        return otpExpiry;
    }

    public void setOtpExpiry(LocalDateTime otpExpiry) {
        this.otpExpiry = otpExpiry;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
package com.QuipBill_server.QuipBill.modules.shopOwner.billing.entity;

import com.QuipBill_server.QuipBill.modules.shopOwner.billing.enums.BillingMode;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(
    name = "bills",
    indexes = {
        @Index(name = "idx_bill_shop", columnList = "shop_id"),
        @Index(name = "idx_bill_created", columnList = "created_at")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // Primary key with auto-increment

    @Column(name = "shop_id", nullable = false)
    private Long shopId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "bill_number", nullable = false, unique = true, length = 32)
    private String billNumber;  // Business-facing bill number

    @Enumerated(EnumType.STRING)
    @Column(name = "billing_mode", nullable = false)
    private BillingMode billingMode;

    @Column(nullable = false)
    private Double subtotal;

    @Column(name = "final_discount", nullable = false)
    private Double finalDiscount;

    @Column(nullable = false)
    private Double roundOff;

    @Column(name = "grand_total", nullable = false)
    private Double grandTotal;

    @Column(name = "final_amount", nullable = false)
    private Double finalAmount;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(
            mappedBy = "bill",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<BillItem> items = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.billNumber == null || this.billNumber.isBlank()) {
            this.billNumber = "BILL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        }
        if (this.finalAmount == null) {
            this.finalAmount = this.grandTotal;
        }
    }
}

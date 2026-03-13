package com.QuipBill_server.QuipBill.modules.shopOwner.billing.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
    name = "bill_items",
    indexes = {
        @Index(name = "idx_billitem_bill", columnList = "bill_id")
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 🔥 Many Items -> One Bill
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", nullable = false)
    @JsonIgnore // 🔥 prevents infinite recursion
    private Bill bill;

    // Optional (null for manual billing)
    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private Integer quantity;

    @Column(name = "discount_percent", nullable = false)
    private Double discountPercent;

    @Column(name = "gst_enabled", nullable = false)
    private Boolean gstEnabled;

    @Column(name = "gst_percent")
    private Double gstPercent;

    @Column(name = "gst_amount")
    private Double gstAmount;

    @Column(name = "final_amount", nullable = false)
    private Double finalAmount;
}
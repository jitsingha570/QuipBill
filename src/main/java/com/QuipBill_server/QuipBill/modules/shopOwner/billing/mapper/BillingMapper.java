package com.QuipBill_server.QuipBill.modules.shopOwner.billing.mapper;

import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillItemRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.entity.Bill;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.entity.BillItem;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BillingMapper {

    public Bill toBillEntity(BillRequest request,
                             Long shopId,
                             double subtotal,
                             double finalDiscount,
                             double roundOff,
                             double grandTotal) {

        return Bill.builder()
                .shopId(shopId)
                .customerName(request.getCustomerName())
                .billingMode(request.getBillingMode())
                .subtotal(subtotal)
                .finalDiscount(finalDiscount)
                .roundOff(roundOff)
                .grandTotal(grandTotal)
                .finalAmount(grandTotal)
                .build();
    }

    public List<BillItem> toBillItems(List<BillItemRequest> itemRequests, Bill bill) {

        return itemRequests.stream()
                .map(item -> BillItem.builder()
                        .bill(bill)
                        .productId(item.getProductId())
                        .productName(item.getProductName())
                        .price(item.getPrice())
                        .quantity(item.getQuantity())
                        .discountPercent(item.getDiscountPercent())
                        .finalAmount(calculateFinalAmount(item))
                        .build())
                .collect(Collectors.toList());
    }

    private double calculateFinalAmount(BillItemRequest item) {
        double total = item.getPrice() * item.getQuantity();
        double discount = (total * item.getDiscountPercent()) / 100;
        return total - discount;
    }
}

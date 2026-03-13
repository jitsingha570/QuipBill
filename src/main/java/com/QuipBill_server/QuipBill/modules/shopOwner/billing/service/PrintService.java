package com.QuipBill_server.QuipBill.modules.shopOwner.billing.service;

import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.PrintableBillResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.entity.Bill;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.entity.BillItem;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class PrintService {

    public PrintableBillResponse preparePrintableBill(Bill bill) {

        return PrintableBillResponse.builder()
                .billId(bill.getId())
                .shopName("QuipBill Store") // Later fetch from Shop table
                .shopAddress("Your Shop Address")
                .shopPhone("9999999999")
                .customerName(bill.getCustomerName())
                .createdAt(bill.getCreatedAt())
                .items(
                        bill.getItems().stream()
                                .map(this::mapToPrintableItem)
                                .collect(Collectors.toList())
                )
                .subtotal(bill.getSubtotal())
                .finalDiscount(bill.getFinalDiscount())
                .roundOff(bill.getRoundOff())
                .grandTotal(bill.getGrandTotal())
                .build();
    }

    private PrintableBillResponse.PrintableItem mapToPrintableItem(BillItem item) {

        return PrintableBillResponse.PrintableItem.builder()
                .productName(item.getProductName())
                .price(item.getPrice())
                .quantity(item.getQuantity())
                .discountPercent(item.getDiscountPercent())
                .finalAmount(item.getFinalAmount())
                .build();
    }
}
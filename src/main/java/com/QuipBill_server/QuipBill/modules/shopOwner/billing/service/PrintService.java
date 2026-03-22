package com.QuipBill_server.QuipBill.modules.shopOwner.billing.service;

import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.PrintableBillResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.entity.Bill;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.entity.BillItem;
import com.QuipBill_server.QuipBill.modules.authentication.entity.Shop;
import com.QuipBill_server.QuipBill.modules.authentication.repository.ShopRepository;
import com.QuipBill_server.QuipBill.common.exception.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrintService {

    private final ShopRepository shopRepository;

    public PrintableBillResponse preparePrintableBill(Bill bill) {

        Shop shop = shopRepository.findById(bill.getShopId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Shop not found"));

        return PrintableBillResponse.builder()
                .billId(bill.getId())
                .shopName(shop.getShopName())
                .shopAddress(shop.getAddress())
                .shopPhone(shop.getMobileNumber())
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

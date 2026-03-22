package com.QuipBill_server.QuipBill.modules.shopOwner.billing.service;

import com.QuipBill_server.QuipBill.common.exception.ApiException;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillItemResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.PrintableBillResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.entity.Bill;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.entity.BillItem;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.repository.BillRepository;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.utils.BillingCalculator;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.validation.BillingValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillingServiceImpl implements BillingService {

    private final BillRepository billRepository;
    private final BillingValidator validator;
    private final BillingCalculator calculator;
    private final PrintService printService;

    @Override
    public BillResponse generateBill(BillRequest request, Long shopId) {

        validator.validateBillRequest(request);

        double subtotal = calculator.calculateSubtotal(request.getItems());
        double afterDiscount = subtotal - request.getFinalDiscount();
        double grandTotal = Math.round(afterDiscount);
        double roundOff = grandTotal - afterDiscount;

        Bill bill = Bill.builder()
                .shopId(shopId)
                .customerName(request.getCustomerName())
                .billingMode(request.getBillingMode())
                .subtotal(subtotal)
                .finalDiscount(request.getFinalDiscount())
                .roundOff(roundOff)
                .grandTotal(grandTotal)
                .finalAmount(grandTotal)
                .build();

        List<BillItem> items = request.getItems().stream()
                .map(itemRequest -> {

                    double finalAmount = calculator.calculateItemFinalAmount(itemRequest);
                    double gstAmount = 0;

                    if (Boolean.TRUE.equals(itemRequest.getGstEnabled())) {
                        double baseTotal = itemRequest.getPrice() * itemRequest.getQuantity();
                        double discountAmount = (baseTotal * itemRequest.getDiscountPercent()) / 100;
                        double afterItemDiscount = baseTotal - discountAmount;
                        gstAmount = (afterItemDiscount * itemRequest.getGstPercent()) / 100;
                    }

                    return BillItem.builder()
                            .bill(bill)
                            .productId(itemRequest.getProductId())
                            .productName(itemRequest.getProductName())
                            .price(itemRequest.getPrice())
                            .quantity(itemRequest.getQuantity())
                            .discountPercent(itemRequest.getDiscountPercent())
                            .gstEnabled(itemRequest.getGstEnabled())
                            .gstPercent(itemRequest.getGstPercent())
                            .gstAmount(gstAmount)
                            .finalAmount(finalAmount)
                            .build();
                })
                .toList();

        bill.setItems(items);
        Bill savedBill = billRepository.save(bill);
        return mapToResponse(savedBill, "Bill generated successfully");
    }

    @Override
    public PrintableBillResponse printBill(Long billId, Long shopId) {

        Bill bill = billRepository
                .findByIdAndShopId(billId, shopId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Bill not found"));

        return printService.preparePrintableBill(bill);
    }

    @Override
    public BillResponse getBillById(Long billId, Long shopId) {

        Bill bill = billRepository
                .findByIdAndShopId(billId, shopId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Bill not found"));

        return mapToResponse(bill, "Bill fetched successfully");
    }

    @Override
    public List<BillResponse> getBillsByDate(LocalDate date, Long shopId) {

        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);

        List<Bill> bills = billRepository.findByShopIdAndCreatedAtBetween(shopId, startOfDay, endOfDay);

        return bills.stream()
                .map(bill -> mapToResponse(bill, "Bill fetched successfully"))
                .toList();
    }

    private BillResponse mapToResponse(Bill bill, String message) {
        return BillResponse.builder()
                .billId(bill.getId())
                .billNumber(bill.getBillNumber())
                .shopId(bill.getShopId())
                .customerName(bill.getCustomerName())
                .billingMode(bill.getBillingMode() != null ? bill.getBillingMode().name() : null)
                .subtotal(bill.getSubtotal())
                .finalDiscount(bill.getFinalDiscount())
                .roundOff(bill.getRoundOff())
                .grandTotal(bill.getGrandTotal())
                .finalAmount(bill.getFinalAmount())
                .createdAt(bill.getCreatedAt())
                .items(bill.getItems() != null
                        ? bill.getItems().stream()
                            .map(item -> BillItemResponse.builder()
                                    .productId(item.getProductId())
                                    .productName(item.getProductName())
                                    .price(item.getPrice())
                                    .quantity(item.getQuantity())
                                    .discountPercent(item.getDiscountPercent())
                                    .gstEnabled(item.getGstEnabled())
                                    .gstPercent(item.getGstPercent())
                                    .gstAmount(item.getGstAmount())
                                    .finalAmount(item.getFinalAmount())
                                    .build())
                            .toList()
                        : List.of())
                .message(message)
                .build();
    }
}

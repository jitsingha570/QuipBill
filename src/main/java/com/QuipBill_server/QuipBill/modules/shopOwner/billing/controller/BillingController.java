package com.QuipBill_server.QuipBill.modules.shopOwner.billing.controller;

import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.PrintableBillResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.service.BillingService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/billing")
@RequiredArgsConstructor
public class BillingController {

    private final BillingService billingService;

    // Generate Bill
    @PostMapping("/generate")
    public ResponseEntity<BillResponse> generateBill(
            @jakarta.validation.Valid @RequestBody BillRequest request,
            Authentication authentication
    ) {

        Long shopId = Long.parseLong(authentication.getName());

        return ResponseEntity.ok(
                billingService.generateBill(request, shopId)
        );
    }

    // Print Bill
    @GetMapping("/print/{billId}")
    public ResponseEntity<PrintableBillResponse> printBill(
            @PathVariable Long billId,
            Authentication authentication
    ) {

        Long shopId = Long.parseLong(authentication.getName());

        return ResponseEntity.ok(
                billingService.printBill(billId, shopId)
        );
    }

    // Get Bill By ID (Secure)
    @GetMapping("/{billId}")
    public ResponseEntity<BillResponse> getBillById(
            @PathVariable Long billId,
            Authentication authentication
    ) {

        Long shopId = Long.parseLong(authentication.getName());

        return ResponseEntity.ok(
                billingService.getBillById(billId, shopId)
        );
    }

    // Get Bills By Date (Secure)
    @GetMapping("/date")
    public ResponseEntity<List<BillResponse>> getBillsByDateQuery(
            @RequestParam String date,
            Authentication authentication
    ) {

        Long shopId = Long.parseLong(authentication.getName());
        return ResponseEntity.ok(
                billingService.getBillsByDate(parseDate(date), shopId)
        );
    }

    // Get Bills By Date (Secure) - path style (use URL-encoded date like 12%2F3%2F2006)
    @GetMapping("/date/{date}")
    public ResponseEntity<List<BillResponse>> getBillsByDate(
            @PathVariable String date,
            Authentication authentication
    ) {

        Long shopId = Long.parseLong(authentication.getName());

        return ResponseEntity.ok(
                billingService.getBillsByDate(parseDate(date), shopId)
        );
    }

    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (java.time.format.DateTimeParseException ignored) {
            // Fall through to d/M/yyyy
        }

        try {
            return LocalDate.parse(date, java.time.format.DateTimeFormatter.ofPattern("d/M/yyyy"));
        } catch (java.time.format.DateTimeParseException ex) {
            throw new com.QuipBill_server.QuipBill.common.exception.ApiException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Invalid date format. Use yyyy-MM-dd or d/M/yyyy, e.g., 2006-03-12 or 12/3/2006"
            );
        }
    }
}

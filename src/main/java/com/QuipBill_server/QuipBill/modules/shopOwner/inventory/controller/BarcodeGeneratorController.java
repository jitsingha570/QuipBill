package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.controller;

import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.BarcodeGenerateRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.BarcodeGenerateResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.service.BarcodeGeneratorService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory/barcode")
@RequiredArgsConstructor
public class BarcodeGeneratorController {

    private final BarcodeGeneratorService barcodeGeneratorService;

    @PostMapping("/generate")
    public BarcodeGenerateResponse generateBarcode(
            @RequestBody BarcodeGenerateRequest request,
            Authentication authentication) {

        Long shopId = Long.parseLong(authentication.getName());
        return barcodeGeneratorService.generate(shopId, request);
    }
}

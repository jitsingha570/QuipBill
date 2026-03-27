package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.controller;

import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.BarcodeLookupResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.service.BarcodeLookupService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory/barcode")
@RequiredArgsConstructor
public class BarcodeController {

    private final BarcodeLookupService barcodeLookupService;

    @GetMapping("/{barcode}")
    public ResponseEntity<BarcodeLookupResponse> lookupProduct(
            @PathVariable String barcode,
            Authentication authentication) {

        Long shopId = Long.parseLong(authentication.getName());
        BarcodeLookupResponse response = barcodeLookupService.lookup(shopId, barcode);

        if (response == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(response);
    }
}

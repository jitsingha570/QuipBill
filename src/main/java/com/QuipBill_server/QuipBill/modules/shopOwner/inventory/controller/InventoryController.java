package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.controller;

import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.InventoryRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.InventoryResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.service.InventoryService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/stock")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // Add stock
    @PutMapping("/add")
    public InventoryResponse addStock(@RequestBody InventoryRequest request) {

        return inventoryService.addStock(request.getProductId(), request.getShopId(), request.getQuantity());
    }

    // Reduce stock
    @PutMapping("/reduce")
    public InventoryResponse reduceStock(@RequestBody InventoryRequest request) {

        return inventoryService.reduceStock(request.getProductId(), request.getShopId(), request.getQuantity());
    }

    // Reduce stock by billing items
    @PutMapping("/reduce-by-billing")
    public List<InventoryResponse> reduceStockByBilling(
            @RequestParam Long shopId,
            @RequestBody BillRequest request) {

        return inventoryService.reduceStockByBilling(request, shopId);
    }

    // Get stock
    @GetMapping("/{productId}")
    public InventoryResponse getStock(@PathVariable Long productId) {
        return inventoryService.getStock(productId);
    }
}

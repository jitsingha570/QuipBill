package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.service;

import com.QuipBill_server.QuipBill.common.exception.ApiException;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillItemRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.InventoryResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.entity.Product;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.repository.ProductRepository;
import com.QuipBill_server.QuipBill.modules.authentication.entity.Shop;
import com.QuipBill_server.QuipBill.modules.authentication.repository.ShopRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    // Add stock
    public InventoryResponse addStock(Long productId, Long shopId, int quantity) {

        Product product = productRepository
                .findByProductIdAndShop_Id(productId, shopId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Product not found"));

        int currentQty = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
        product.setStockQuantity(currentQty + quantity);

        Product saved = productRepository.save(product);

        return mapToResponse(saved);
    }

    // Reduce stock
    public InventoryResponse reduceStock(Long productId, Long shopId, int quantity) {

        Product product = productRepository
                .findByProductIdAndShop_Id(productId, shopId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Product not found"));

        int currentQty = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
        if (currentQty < quantity) {
            throw new ApiException(HttpStatus.CONFLICT, "Insufficient stock");
        }

        product.setStockQuantity(currentQty - quantity);

        Product saved = productRepository.save(product);

        return mapToResponse(saved);
    }

    // Get stock
    public InventoryResponse getStock(Long productId, Long shopId) {

        Product product = productRepository
                .findByProductIdAndShop_Id(productId, shopId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Product not found"));

        return mapToResponse(product);
    }

    // Reduce stock by billing items
    public List<InventoryResponse> reduceStockByBilling(BillRequest request, Long shopId) {

        if (request == null || request.getItems() == null || request.getItems().isEmpty()) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "Bill items are required");
        }

        List<InventoryResponse> responses = new ArrayList<>();

        for (BillItemRequest item : request.getItems()) {
            if (item == null || item.getQuantity() == null
                    || (item.getProductId() == null && (item.getProductName() == null || item.getProductName().isBlank()))) {
                throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid bill item");
            }

            Product product = findProductForBilling(item, shopId);

            int currentQty = product.getStockQuantity() != null ? product.getStockQuantity() : 0;
            int newQty = currentQty;
            if (currentQty > 0 && item.getQuantity() != null && item.getQuantity() > 0) {
                newQty = Math.max(0, currentQty - item.getQuantity());
            }
            product.setStockQuantity(newQty);
            Product saved = productRepository.save(product);
            responses.add(mapToResponse(saved));
        }

        return responses;
    }

    private Product findProductForBilling(BillItemRequest item, Long shopId) {
        if (item.getProductId() != null) {
            return productRepository
                    .findByProductIdAndShop_Id(item.getProductId(), shopId)
                    .orElseGet(() -> findOrCreateByName(item, shopId));
        }

        return findOrCreateByName(item, shopId);
    }

    private Product findOrCreateByName(BillItemRequest item, Long shopId) {
        String productName = item.getProductName();
        if (productName == null || productName.isBlank()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Product not found");
        }

        return productRepository
                .findByProductNameAndShop_Id(productName.trim(), shopId)
                .orElseGet(() -> createProductFromBillItem(item, shopId));
    }

    private Product createProductFromBillItem(BillItemRequest item, Long shopId) {
        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Shop not found"));

        Double gstPercent = item.getGstPercent();
        Boolean gstEnabled = item.getGstEnabled();
        if (gstPercent == null || gstPercent <= 0) {
            gstPercent = 0.0;
            gstEnabled = false;
        } else if (gstEnabled == null) {
            gstEnabled = true;
        }

        Product product = Product.builder()
                .shop(shop)
                .productName(item.getProductName().trim())
                .barcode(null)
                .price(item.getPrice())
                .gstPercent(gstPercent)
                .gstEnabled(gstEnabled)
                .stockQuantity(item.getQuantity() != null ? item.getQuantity() : 0)
                .build();

        return productRepository.save(product);
    }

    private InventoryResponse mapToResponse(Product product) {

        return InventoryResponse.builder()
                .productId(product.getProductId())
                .shopId(product.getShop() != null ? product.getShop().getId() : null)
                .productName(product.getProductName())
                .barcode(product.getBarcode())
                .quantity(product.getStockQuantity())
                .price(product.getPrice())
                .build();
    }
}

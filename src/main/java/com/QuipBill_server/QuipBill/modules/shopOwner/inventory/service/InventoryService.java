package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.service;

import com.QuipBill_server.QuipBill.common.exception.ApiException;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillItemRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.InventoryResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.entity.Product;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.repository.ProductRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;

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
    public InventoryResponse getStock(Long productId) {

        Product product = productRepository.findById(productId)
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
            if (currentQty < item.getQuantity()) {
                throw new ApiException(HttpStatus.CONFLICT, "Insufficient stock");
            }

            product.setStockQuantity(currentQty - item.getQuantity());
            Product saved = productRepository.save(product);
            responses.add(mapToResponse(saved));
        }

        return responses;
    }

    private Product findProductForBilling(BillItemRequest item, Long shopId) {
        if (item.getProductId() != null) {
            return productRepository
                    .findByProductIdAndShop_Id(item.getProductId(), shopId)
                    .orElseGet(() -> findByExactName(item.getProductName(), shopId));
        }

        return findByExactName(item.getProductName(), shopId);
    }

    private Product findByExactName(String productName, Long shopId) {
        if (productName == null || productName.isBlank()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "Product not found");
        }

        return productRepository
                .findByProductNameAndShop_Id(productName.trim(), shopId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Product not found"));
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

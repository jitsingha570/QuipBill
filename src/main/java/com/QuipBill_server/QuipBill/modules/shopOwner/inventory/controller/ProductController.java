package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.controller;

import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.ProductRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.ProductResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.ProductSearchResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // Add product manually
    @PostMapping
    public ProductResponse addProduct(
            @RequestBody ProductRequest request,
            Authentication authentication) {
        Long shopId = Long.parseLong(authentication.getName());
        return productService.createProduct(shopId, request);
    }

    // Get all products
    @GetMapping
    public List<ProductResponse> getAllProducts(Authentication authentication) {
        Long shopId = Long.parseLong(authentication.getName());
        return productService.getAllProducts(shopId);
    }

    // 🔍 Search products (Auto-suggestion for billing)
    @GetMapping("/search")
    public List<ProductSearchResponse> searchProducts(
            @RequestParam String keyword,
            Authentication authentication) {
        Long shopId = Long.parseLong(authentication.getName());

        // ✅ Avoid unnecessary DB calls
        if (keyword == null || keyword.trim().length() < 2) {
            return List.of();
        }

        return productService.searchProducts(keyword.trim(), shopId);
    }

    // Get product by ID
    @GetMapping("/{productId}")
    public ProductResponse getProduct(
            @PathVariable Long productId,
            Authentication authentication) {
        Long shopId = Long.parseLong(authentication.getName());
        return productService.getProductById(shopId, productId);
    }

    // Update product
    @PutMapping("/{productId}")
    public ProductResponse updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductRequest request,
            Authentication authentication) {

        Long shopId = Long.parseLong(authentication.getName());
        return productService.updateProduct(shopId, productId, request);
    }

    // Delete product
    @DeleteMapping("/{productId}")
    public void deleteProduct(
            @PathVariable Long productId,
            Authentication authentication) {
        Long shopId = Long.parseLong(authentication.getName());
        productService.deleteProduct(shopId, productId);
    }
}

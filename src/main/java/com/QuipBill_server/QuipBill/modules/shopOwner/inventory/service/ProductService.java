package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.service;

import com.QuipBill_server.QuipBill.common.exception.ApiException;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.ProductRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.ProductResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.ProductSearchResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.entity.Product;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.repository.ProductRepository;
import com.QuipBill_server.QuipBill.modules.authentication.entity.Shop;
import com.QuipBill_server.QuipBill.modules.authentication.repository.ShopRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    // Create product
    public ProductResponse createProduct(Long shopId, ProductRequest request) {

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Shop not found"));

        String barcode = normalizeBarcode(request.getBarcode());
        if (barcode != null) {
            productRepository.findByBarcodeAndShop_Id(barcode, shopId)
                    .ifPresent(p -> {
                        throw new ApiException(HttpStatus.CONFLICT, "Barcode already exists for this shop");
                    });
        }

        Product product = Product.builder()
                .shop(shop)
                .productName(request.getProductName())
                .barcode(barcode)
                .price(request.getPrice())
                .gstPercent(request.getGstPercent())
                .gstEnabled(request.getGstEnabled())
                .stockQuantity(request.getQuantity() != null ? request.getQuantity() : 0)
                .build();

        return mapToResponse(productRepository.save(product));
    }

    // Get all products
    public List<ProductResponse> getAllProducts(Long shopId) {
        return productRepository.findByShop_Id(shopId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // 🔍 SEARCH API (UPDATED 🔥)
    public List<ProductSearchResponse> searchProducts(String keyword, Long shopId) {

        if (keyword == null || keyword.trim().length() < 2) {
            return List.of();
        }

        return productRepository
                .findTop10ByProductNameStartingWithIgnoreCaseAndShop_Id(keyword.trim(), shopId)
                .stream()
                .map(this::mapToSearchResponse)
                .toList();
    }

    // Get by ID
    public ProductResponse getProductById(Long shopId, Long productId) {

        Product product = productRepository.findByProductIdAndShop_Id(productId, shopId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Product not found"));

        return mapToResponse(product);
    }

    // Update
    public ProductResponse updateProduct(Long shopId, Long productId, ProductRequest request) {

        Product product = productRepository.findByProductIdAndShop_Id(productId, shopId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Product not found"));

        String barcode = normalizeBarcode(request.getBarcode());
        if (barcode != null) {
            productRepository.findByBarcodeAndShop_Id(barcode, shopId)
                    .ifPresent(existing -> {
                        if (!existing.getProductId().equals(productId)) {
                            throw new ApiException(HttpStatus.CONFLICT, "Barcode already exists for this shop");
                        }
                    });
        }

        product.setProductName(request.getProductName());
        product.setBarcode(barcode);
        product.setPrice(request.getPrice());
        product.setGstPercent(request.getGstPercent());
        product.setGstEnabled(request.getGstEnabled());

        if (request.getQuantity() != null) {
            product.setStockQuantity(request.getQuantity());
        }

        return mapToResponse(productRepository.save(product));
    }

    // Delete
    @Transactional
    public void deleteProduct(Long shopId, Long productId) {

        Product product = productRepository.findByProductIdAndShop_Id(productId, shopId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Product not found"));

        productRepository.delete(product);
    }

    // 🔄 Mapper → Full response
    private ProductResponse mapToResponse(Product product) {

        return ProductResponse.builder()
                .productId(product.getProductId())
                .shopId(product.getShop() != null ? product.getShop().getId() : null)
                .productName(product.getProductName())
                .barcode(product.getBarcode())
                .price(product.getPrice())
                .gstPercent(product.getGstPercent())
                .gstEnabled(product.getGstEnabled())
                .quantity(product.getStockQuantity())
                .build();
    }

    // 🔄 Mapper → Search response (LIGHTWEIGHT 🔥)
    private ProductSearchResponse mapToSearchResponse(Product product) {

        return new ProductSearchResponse(
                product.getProductId(),
                product.getProductName(),
                product.getPrice(),
                product.getGstPercent(),
                product.getGstEnabled(),
                product.getStockQuantity()
        );
    }

    private String normalizeBarcode(String barcode) {
        if (barcode == null) {
            return null;
        }
        String normalized = barcode.trim();
        return normalized.isBlank() ? null : normalized;
    }
}

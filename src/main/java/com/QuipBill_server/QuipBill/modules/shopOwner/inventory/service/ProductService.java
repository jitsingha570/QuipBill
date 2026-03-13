package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.service;

import com.QuipBill_server.QuipBill.common.exception.ApiException;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.ProductRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.ProductResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.entity.Product;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.repository.ProductRepository;
import com.QuipBill_server.QuipBill.modules.authentication.entity.Shop;
import com.QuipBill_server.QuipBill.modules.authentication.repository.ShopRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ShopRepository shopRepository;

    // Create product manually
    public ProductResponse createProduct(ProductRequest request) {

        Shop shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Shop not found"));

        Product product = Product.builder()
                .shop(shop)
                .productName(request.getProductName())
                .barcode(request.getBarcode())
                .price(request.getPrice())
                .gstPercent(request.getGstPercent())
                .gstEnabled(request.getGstEnabled())
                .stockQuantity(request.getQuantity() != null ? request.getQuantity() : 0)
                .build();

        Product saved = productRepository.save(product);

        return mapToResponse(saved);
    }

    // Get all products
    public List<ProductResponse> getAllProducts(Long shopId) {

        return productRepository.findByShop_Id(shopId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Get product by ID
    public ProductResponse getProductById(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Product not found"));

        return mapToResponse(product);
    }

    // Update product
    public ProductResponse updateProduct(Long productId, ProductRequest request) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Product not found"));

        if (request.getShopId() != null && !request.getShopId().equals(product.getShop().getId())) {
            throw new ApiException(HttpStatus.FORBIDDEN, "Shop mismatch for product");
        }

        product.setProductName(request.getProductName());
        product.setBarcode(request.getBarcode());
        product.setPrice(request.getPrice());
        product.setGstPercent(request.getGstPercent());
        product.setGstEnabled(request.getGstEnabled());
        if (request.getQuantity() != null) {
            product.setStockQuantity(request.getQuantity());
        }

        Product updated = productRepository.save(product);

        return mapToResponse(updated);
    }

    // Delete product
    @Transactional
    public void deleteProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Product not found"));

        productRepository.delete(product);
    }

    // Mapper
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
}

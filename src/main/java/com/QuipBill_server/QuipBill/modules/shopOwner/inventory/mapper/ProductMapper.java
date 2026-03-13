package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.mapper;

import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.ProductRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.ProductResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.entity.Product;



//purpose conver entity to dto 
public class ProductMapper {

    public static Product toEntity(ProductRequest request) {

        return Product.builder()
                .productName(request.getProductName())
                .barcode(request.getBarcode())
                .price(request.getPrice())
                .gstPercent(request.getGstPercent())
                .gstEnabled(request.getGstEnabled())
                .build();
    }

    public static ProductResponse toResponse(Product product) {

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

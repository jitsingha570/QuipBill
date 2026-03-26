package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.service;

import com.QuipBill_server.QuipBill.common.exception.ApiException;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.BarcodeGenerateRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.BarcodeGenerateResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.entity.Product;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.repository.ProductRepository;
import com.QuipBill_server.QuipBill.modules.authentication.entity.Shop;
import com.QuipBill_server.QuipBill.modules.authentication.repository.ShopRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BarcodeGeneratorService {

    private final ProductRepository productRepository;
    private final BarcodeLabelService barcodeLabelService;
    private final ShopRepository shopRepository;

    public BarcodeGenerateResponse generate(Long shopId, BarcodeGenerateRequest request) {

        // Generate unique barcode
        String barcode = "900" + System.currentTimeMillis();

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Shop not found"));

        Product product = Product.builder()
                .shop(shop)
                .productName(request.getProductName())
                .barcode(barcode)
                .price(request.getPrice())
                .gstEnabled(true)
                .stockQuantity(0)
                .build();

        Product saved = productRepository.save(product);

        String labelUrl = barcodeLabelService.generateLabel(barcode);

        return BarcodeGenerateResponse.builder()
                .productId(saved.getProductId())
                .barcode(barcode)
                .labelUrl(labelUrl)
                .build();
    }
}

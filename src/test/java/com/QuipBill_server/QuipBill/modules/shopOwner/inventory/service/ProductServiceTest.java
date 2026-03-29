package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.service;

import com.QuipBill_server.QuipBill.modules.authentication.entity.Shop;
import com.QuipBill_server.QuipBill.modules.authentication.repository.ShopRepository;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillItemRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.ProductRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.entity.Product;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Test
    void createProduct_usesAuthenticatedShopId_notRequestShopId() {
        ProductRepository productRepository = mock(ProductRepository.class);
        ShopRepository shopRepository = mock(ShopRepository.class);

        ProductService service = new ProductService(productRepository, shopRepository);

        Long authenticatedShopId = 10L;
        Long requestShopId = 999L;

        Shop shop = new Shop();
        shop.setEmail("test@example.com");
        shop.setPassword("x");
        shop.setShopName("Shop");

        when(shopRepository.findById(authenticatedShopId)).thenReturn(Optional.of(shop));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));
        when(productRepository.findByBarcodeAndShop_Id("123", authenticatedShopId)).thenReturn(Optional.empty());

        ProductRequest request = ProductRequest.builder()
                .shopId(requestShopId)
                .productName("P")
                .barcode("123")
                .price(10.0)
                .quantity(1)
                .build();

        service.createProduct(authenticatedShopId, request);

        verify(shopRepository).findById(authenticatedShopId);
        verify(shopRepository, never()).findById(requestShopId);

        ArgumentCaptor<Product> saved = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(saved.capture());
        assertSame(shop, saved.getValue().getShop());
    }

    @Test
    void createProduct_blankBarcode_isSavedAsNull() {
        ProductRepository productRepository = mock(ProductRepository.class);
        ShopRepository shopRepository = mock(ShopRepository.class);

        ProductService service = new ProductService(productRepository, shopRepository);

        Long authenticatedShopId = 10L;

        Shop shop = new Shop();
        shop.setEmail("test@example.com");
        shop.setPassword("x");
        shop.setShopName("Shop");

        when(shopRepository.findById(authenticatedShopId)).thenReturn(Optional.of(shop));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductRequest request = ProductRequest.builder()
                .productName("P")
                .barcode("   ")
                .price(10.0)
                .quantity(1)
                .build();

        service.createProduct(authenticatedShopId, request);

        verify(productRepository, never()).findByBarcodeAndShop_Id(anyString(), anyLong());

        ArgumentCaptor<Product> saved = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(saved.capture());
        assertNull(saved.getValue().getBarcode());
    }

    @Test
    void resolveProductForBilling_reusesExistingBarcodeInsteadOfCreatingDuplicate() {
        ProductRepository productRepository = mock(ProductRepository.class);
        ShopRepository shopRepository = mock(ShopRepository.class);

        ProductService service = new ProductService(productRepository, shopRepository);

        Long shopId = 10L;
        Product existing = Product.builder()
                .productId(55L)
                .productName("Milk")
                .barcode("890123")
                .price(42.0)
                .build();

        when(productRepository.findByBarcodeAndShop_Id("890123", shopId)).thenReturn(Optional.of(existing));

        BillItemRequest request = BillItemRequest.builder()
                .productName("Milk")
                .barcode("890123")
                .price(42.0)
                .quantity(1)
                .build();

        Product resolved = service.resolveProductForBilling(shopId, request);

        assertSame(existing, resolved);
        verify(productRepository, never()).saveAndFlush(any(Product.class));
    }
}

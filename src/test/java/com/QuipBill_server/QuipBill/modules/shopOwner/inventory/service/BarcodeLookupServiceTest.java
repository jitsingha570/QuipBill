package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.service;

import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.dto.BarcodeLookupResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.entity.Product;
import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.repository.ProductRepository;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BarcodeLookupServiceTest {

    @Test
    void lookup_scopesBarcodeToShop() {
        ProductRepository productRepository = mock(ProductRepository.class);
        BarcodeLookupService service = new BarcodeLookupService(productRepository);

        Long shopId = 123L;
        String barcode = "8901234567890";

        when(productRepository.findByBarcodeAndShop_Id(barcode, shopId))
                .thenReturn(Optional.of(Product.builder().productId(1L).barcode(barcode).productName("Test").build()));

        BarcodeLookupResponse response = service.lookup(shopId, barcode);

        assertNotNull(response);
        assertEquals(Boolean.TRUE, response.getExists());

        verify(productRepository).findByBarcodeAndShop_Id(barcode, shopId);
        verify(productRepository, never()).findByBarcode(anyString());
    }
}

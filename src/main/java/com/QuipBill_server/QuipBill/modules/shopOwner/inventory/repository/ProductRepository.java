package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.repository;

import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Existing methods ✅
    Optional<Product> findByBarcode(String barcode);

    Optional<Product> findByBarcodeAndShop_Id(String barcode, Long shopId);

    List<Product> findByShop_Id(Long shopId);

    Optional<Product> findByProductIdAndShop_Id(Long productId, Long shopId);

    Optional<Product> findByProductNameAndShop_Id(String productName, Long shopId);

    // 🔥 NEW: Search for billing (AUTO-SUGGESTION)

    // ⚡ FAST (Recommended)
    List<Product> findTop10ByProductNameStartingWithIgnoreCaseAndShop_Id(
            String keyword, Long shopId
    );

    // 🔍 OPTIONAL (Flexible but slower)
    List<Product> findTop10ByProductNameContainingIgnoreCaseAndShop_Id(
            String keyword, Long shopId
    );
}

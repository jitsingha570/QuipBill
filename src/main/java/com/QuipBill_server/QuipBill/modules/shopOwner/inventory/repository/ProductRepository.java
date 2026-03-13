package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.repository;

import com.QuipBill_server.QuipBill.modules.shopOwner.inventory.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByBarcode(String barcode);

    List<Product> findByShop_Id(Long shopId);

    Optional<Product> findByProductIdAndShop_Id(Long productId, Long shopId);

    Optional<Product> findByProductNameAndShop_Id(String productName, Long shopId);

}

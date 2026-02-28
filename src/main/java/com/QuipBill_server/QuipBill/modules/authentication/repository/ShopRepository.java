package com.QuipBill_server.QuipBill.modules.authentication.repository;

import com.QuipBill_server.QuipBill.modules.authentication.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    Optional<Shop> findByUsername(String username);

    Optional<Shop> findByEmail(String email);   // ✅ ADD THIS

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
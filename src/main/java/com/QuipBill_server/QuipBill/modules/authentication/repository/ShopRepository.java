package com.QuipBill_server.QuipBill.modules.authentication.repository;
//this file use for database operation for shop entity
import com.QuipBill_server.QuipBill.modules.authentication.entity.Shop;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShopRepository extends JpaRepository<Shop, Long> {

    Optional<Shop> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Shop> findByDashboardPin(String dashboardPin);
}

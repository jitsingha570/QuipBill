package com.QuipBill_server.QuipBill.modules.shopOwner.billing.repository;

import com.QuipBill_server.QuipBill.modules.shopOwner.billing.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {

    // Get all bills of a shop
    List<Bill> findByShopId(Long shopId);

    // Secure: Get bill by ID and shop
    Optional<Bill> findByIdAndShopId(Long id, Long shopId);

    // Secure: Get bills of shop by date
    List<Bill> findByShopIdAndCreatedAtBetween(
            Long shopId,
            LocalDateTime start,
            LocalDateTime end
    );
}

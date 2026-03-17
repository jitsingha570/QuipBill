package com.QuipBill_server.QuipBill.modules.hardware.printer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.QuipBill_server.QuipBill.modules.hardware.printer.entity.PrintTemplate;

import java.util.Optional;

@Repository
public interface PrintTemplateRepository extends JpaRepository<PrintTemplate, Long> {

    // Find template by shop
    Optional<PrintTemplate> findByShopId(Long shopId);

}
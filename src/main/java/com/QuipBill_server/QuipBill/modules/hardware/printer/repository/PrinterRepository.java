package com.QuipBill_server.QuipBill.modules.hardware.printer.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.QuipBill_server.QuipBill.modules.hardware.printer.entity.Printer;

import java.util.List;
import java.util.Optional;

@Repository
public interface PrinterRepository extends JpaRepository<Printer, Long> {

    // Find printers by shop
    List<Printer> findByShopId(Long shopId);

    // Find default printer for shop
    Optional<Printer> findByShopIdAndDefaultPrinterTrue(Long shopId);

    // Find printer by name
    Optional<Printer> findByPrinterName(String printerName);

}
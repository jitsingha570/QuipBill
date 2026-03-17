package com.QuipBill_server.QuipBill.modules.hardware.printer.controller;

import com.QuipBill_server.QuipBill.modules.hardware.printer.dto.PrintLabelRequest;
import com.QuipBill_server.QuipBill.modules.hardware.printer.dto.PrintReceiptRequest;
import com.QuipBill_server.QuipBill.modules.hardware.printer.dto.PrinterRequest;
import com.QuipBill_server.QuipBill.modules.hardware.printer.service.LabelPrintService;
import com.QuipBill_server.QuipBill.modules.hardware.printer.service.PrinterService;
import com.QuipBill_server.QuipBill.modules.hardware.printer.service.ReceiptPrintService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/printer")
public class PrinterController {

    private final PrinterService printerService;
    private final ReceiptPrintService receiptPrintService;
  private final LabelPrintService labelPrintService;

     public PrinterController(
            PrinterService printerService,
            ReceiptPrintService receiptPrintService,
            LabelPrintService labelPrintService) {

        this.printerService = printerService;
        this.receiptPrintService = receiptPrintService;
        this.labelPrintService = labelPrintService;
    }
    /*
     * 1️⃣ Find all available printers
     */
    @GetMapping("/devices")
    public ResponseEntity<List<String>> getAvailablePrinters() {

        List<String> printers = printerService.getAvailablePrinters();

        return ResponseEntity.ok(printers);
    }


    /*
     * 2️⃣ Connect to printer
     */
    @PostMapping("/connect")
    public ResponseEntity<String> connectPrinter(
            @RequestBody PrinterRequest request) {

        printerService.connectPrinter(request.getPrinterName());

        return ResponseEntity.ok("Printer connected successfully");
    }


    /*
     * 3️⃣ Print receipt
     */
    @PostMapping("/print/receipt")
    public ResponseEntity<String> printReceipt(
            @RequestBody PrintReceiptRequest request) {

       
        return ResponseEntity.ok("Receipt printed successfully");
    }


    /*
     * 4️⃣ Print label
     */
    @PostMapping("/print/label")
    public ResponseEntity<String> printLabel(
            @RequestBody PrintLabelRequest request) {

        labelPrintService.printLabel(request);

        return ResponseEntity.ok("Label printed successfully");
    }

}
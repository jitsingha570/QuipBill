package com.QuipBill_server.QuipBill.modules.hardware.printer.service;

import com.QuipBill_server.QuipBill.modules.hardware.printer.formatter.receipt.ReceiptFormatter;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.PrintableBillResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.print.*;

@Service
@RequiredArgsConstructor
public class ReceiptPrintService {

    private final PrinterService printerService;

    private final ReceiptFormatter receiptFormatter = new ReceiptFormatter();

    public void printReceipt(PrintableBillResponse bill) {

        try {

            String receiptText = receiptFormatter.formatReceipt(bill);

            PrintService printer = printerService.getConnectedPrinter();

            DocFlavor flavor = DocFlavor.STRING.TEXT_PLAIN;

            Doc doc = new SimpleDoc(receiptText, flavor, null);

            DocPrintJob job = printer.createPrintJob();

            job.print(doc, null);

        } catch (Exception e) {

            throw new RuntimeException("Receipt printing failed", e);
        }
    }
}
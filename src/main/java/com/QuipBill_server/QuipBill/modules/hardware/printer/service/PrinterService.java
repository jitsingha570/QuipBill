package com.QuipBill_server.QuipBill.modules.hardware.printer.service;

import org.springframework.stereotype.Service;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.util.ArrayList;
import java.util.List;

@Service
public class PrinterService {

    private PrintService connectedPrinter;

    public List<String> getAvailablePrinters() {

        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

        List<String> printers = new ArrayList<>();

        for (PrintService printer : services) {
            printers.add(printer.getName());
        }

        return printers;
    }

    public void connectPrinter(String printerName) {

        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);

        for (PrintService printer : services) {

            if (printer.getName().equalsIgnoreCase(printerName)) {
                connectedPrinter = printer;
                return;
            }
        }

        throw new RuntimeException("Printer not found");
    }

    public PrintService getConnectedPrinter() {

        if (connectedPrinter == null) {
            throw new RuntimeException("No printer connected");
        }

        return connectedPrinter;
    }
}
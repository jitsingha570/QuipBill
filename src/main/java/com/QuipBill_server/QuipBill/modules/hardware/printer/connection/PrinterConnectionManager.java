package com.QuipBill_server.QuipBill.modules.hardware.printer.connection;

import org.springframework.stereotype.Component;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

@Component
public class PrinterConnectionManager {

    private PrintService connectedPrinter;

    public PrintService connect(String printerName) {

        PrintService[] services =
                PrintServiceLookup.lookupPrintServices(null, null);

        for (PrintService printer : services) {

            if (printer.getName().equalsIgnoreCase(printerName)) {

                connectedPrinter = printer;
                return connectedPrinter;
            }
        }

        throw new RuntimeException("Printer not found: " + printerName);
    }

    public PrintService getConnectedPrinter() {

        if (connectedPrinter == null) {
            throw new RuntimeException("No printer connected");
        }

        return connectedPrinter;
    }

    public boolean isConnected() {
        return connectedPrinter != null;
    }
}
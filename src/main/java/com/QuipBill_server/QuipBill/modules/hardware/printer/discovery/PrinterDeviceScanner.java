package com.QuipBill_server.QuipBill.modules.hardware.printer.discovery;

import org.springframework.stereotype.Component;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.util.ArrayList;
import java.util.List;

@Component
public class PrinterDeviceScanner {

    public List<String> scanPrinters() {

        PrintService[] services =
                PrintServiceLookup.lookupPrintServices(null, null);

        List<String> printerNames = new ArrayList<>();

        for (PrintService service : services) {
            printerNames.add(service.getName());
        }

        return printerNames;
    }
}
package com.QuipBill_server.QuipBill.modules.hardware.printer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.QuipBill_server.QuipBill.modules.hardware.printer.dto.PrintLabelRequest;
import com.QuipBill_server.QuipBill.modules.hardware.printer.formatter.label.LabelFormatter;

import javax.print.*;

@Service
@RequiredArgsConstructor
public class LabelPrintService {

    private final PrinterService printerService;

    private final LabelFormatter labelFormatter = new LabelFormatter();

    public void printLabel(PrintLabelRequest label) {

        try {

            //String labelText = labelFormatter.formatLabel(label);

            PrintService printer = printerService.getConnectedPrinter();

            DocFlavor flavor = DocFlavor.STRING.TEXT_PLAIN;

           // Doc doc = new SimpleDoc(labelText, flavor, null);

            DocPrintJob job = printer.createPrintJob();

            //job.print(doc, null);

        } catch (Exception e) {

            throw new RuntimeException("Label printing failed", e);
        }
    }
}
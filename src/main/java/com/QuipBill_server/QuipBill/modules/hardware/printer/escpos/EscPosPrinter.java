package com.QuipBill_server.QuipBill.modules.hardware.printer.escpos;

import org.springframework.stereotype.Component;

import javax.print.*;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@Component
public class EscPosPrinter {

    public void printText(PrintService printer, String text) {

        try {

            ByteArrayOutputStream output = new ByteArrayOutputStream();

            output.write(EscPosCommandBuilder.INIT);

            output.write(text.getBytes(StandardCharsets.UTF_8));

            output.write(EscPosCommandBuilder.CUT_PAPER);

            byte[] bytes = output.toByteArray();

            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;

            Doc doc = new SimpleDoc(bytes, flavor, null);

            DocPrintJob job = printer.createPrintJob();

            job.print(doc, null);

        } catch (Exception e) {

            throw new RuntimeException("ESC/POS printing failed", e);
        }
    }
}
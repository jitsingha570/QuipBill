package com.QuipBill_server.QuipBill.modules.hardware.printer.formatter.receipt;

import org.springframework.stereotype.Component;

@Component
public class ReceiptTemplateManager {

    public String getHeader(String shopName) {

        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append(center(shopName)).append("\n");
        sb.append(center("Powered by QuipBill")).append("\n");
        sb.append("----------------------------------------\n");

        return sb.toString();
    }

    public String getFooter() {

        StringBuilder sb = new StringBuilder();

        sb.append("----------------------------------------\n");
        sb.append(center("Thank You")).append("\n");
        sb.append(center("Visit Again")).append("\n\n\n");

        return sb.toString();
    }

    private String center(String text) {

        int width = 40;

        if (text.length() >= width) return text;

        int padding = (width - text.length()) / 2;

        return " ".repeat(padding) + text;
    }
}
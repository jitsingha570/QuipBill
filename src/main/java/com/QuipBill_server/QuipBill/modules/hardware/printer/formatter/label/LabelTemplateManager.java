package com.QuipBill_server.QuipBill.modules.hardware.printer.formatter.label;

import org.springframework.stereotype.Component;

@Component
public class LabelTemplateManager {

    public String getHeader(String shopName) {

        StringBuilder sb = new StringBuilder();

        sb.append("\n");
        sb.append(center(shopName)).append("\n");
        sb.append("--------------------------------\n");

        return sb.toString();
    }

    public String getFooter() {

        StringBuilder sb = new StringBuilder();

        sb.append("--------------------------------\n");
        sb.append(center("Powered by QuipBill")).append("\n\n");

        return sb.toString();
    }

    private String center(String text) {

        int width = 32;

        if (text == null) return "";

        if (text.length() >= width) return text;

        int padding = (width - text.length()) / 2;

        return " ".repeat(padding) + text;
    }
}
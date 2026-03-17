package com.QuipBill_server.QuipBill.modules.hardware.printer.formatter.label;

import com.QuipBill_server.QuipBill.modules.hardware.printer.dto.PrintLabelRequest;

public class LabelFormatter {

    public String formatLabelText(PrintLabelRequest label) {

        StringBuilder sb = new StringBuilder();

        sb.append(center(label.getShopName())).append("\n");
        sb.append("------------------------------\n");

        sb.append("Product : ").append(label.getProductName()).append("\n");

        sb.append("------------------------------\n");

        sb.append(center("Price: ₹" + label.getPrice())).append("\n");

        return sb.toString();
    }

    private String center(String text){

        int width = 32;

        if(text.length() >= width) return text;

        int padding = (width - text.length()) / 2;

        return " ".repeat(padding) + text;
    }
}
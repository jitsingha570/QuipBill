package com.QuipBill_server.QuipBill.modules.hardware.printer.formatter.receipt;

import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.PrintableBillResponse;

import java.time.format.DateTimeFormatter;

public class ReceiptFormatter {

    private static final int WIDTH = 40;

    public String formatReceipt(PrintableBillResponse bill) {

        StringBuilder sb = new StringBuilder();

        // Shop Details
        sb.append(center(bill.getShopName())).append("\n");
        sb.append(center(bill.getShopAddress())).append("\n");
        sb.append(center("Ph: " + bill.getShopPhone())).append("\n");

        sb.append("----------------------------------------\n");

        // Bill Info
        sb.append("Bill ID : ").append(bill.getBillId()).append("\n");
        sb.append("Customer: ").append(bill.getCustomerName()).append("\n");

        sb.append("Date    : ")
                .append(bill.getCreatedAt()
                        .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")))
                .append("\n");

        sb.append("----------------------------------------\n");

        sb.append(String.format("%-16s %4s %6s %8s\n",
                "Item", "Qty", "Price", "Total"));

        sb.append("----------------------------------------\n");

        // Items
        for (PrintableBillResponse.PrintableItem item : bill.getItems()) {

            sb.append(String.format("%-16s %4d %6.2f %8.2f\n",
                    item.getProductName(),
                    item.getQuantity(),
                    item.getPrice(),
                    item.getFinalAmount()));

            if (item.getDiscountPercent() != null && item.getDiscountPercent() > 0) {

                sb.append(String.format("   Discount %.0f%%\n",
                        item.getDiscountPercent()));
            }
        }

        sb.append("----------------------------------------\n");

        // Subtotal
        sb.append(String.format("%-25s %10.2f\n", "Subtotal", bill.getSubtotal()));

        // GST (ONLY IF ENABLED)
       

        // Discount
        sb.append(String.format("%-25s %10.2f\n", "Discount", bill.getFinalDiscount()));

        // Round off
        sb.append(String.format("%-25s %10.2f\n", "Round Off", bill.getRoundOff()));

        sb.append("----------------------------------------\n");

        // Grand total
        sb.append(String.format("%-25s %10.2f\n", "TOTAL", bill.getGrandTotal()));

        sb.append("----------------------------------------\n");

        sb.append(center("Thank You Visit Again")).append("\n\n\n");

        return sb.toString();
    }

    private String center(String text) {

        if (text == null) return "";

        if (text.length() >= WIDTH) return text;

        int padding = (WIDTH - text.length()) / 2;

        return " ".repeat(padding) + text;
    }
}
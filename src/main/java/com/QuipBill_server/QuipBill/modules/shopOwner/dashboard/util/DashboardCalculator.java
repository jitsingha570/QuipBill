package com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.util;

import com.QuipBill_server.QuipBill.modules.shopOwner.billing.entity.Bill;

import java.util.List;

public class DashboardCalculator {

    // Calculate total sales from bills
    public static double calculateTotalSales(List<Bill> bills) {

        double total = 0;

        for (Bill bill : bills) {
            if (bill.getFinalAmount() != null) {
                total += bill.getFinalAmount();
            }
        }

        return total;
    }

    // Count bills
    public static int countBills(List<Bill> bills) {
        return bills.size();
    }
}
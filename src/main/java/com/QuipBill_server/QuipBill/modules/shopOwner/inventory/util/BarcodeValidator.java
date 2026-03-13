package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.util;
//purpose :- validate scanned barcode 
public class BarcodeValidator {

    public static boolean isValid(String barcode) {

        if(barcode == null || barcode.isEmpty())
            return false;

        if(barcode.length() < 8 || barcode.length() > 20)
            return false;

        return barcode.matches("[0-9]+");
    }
}
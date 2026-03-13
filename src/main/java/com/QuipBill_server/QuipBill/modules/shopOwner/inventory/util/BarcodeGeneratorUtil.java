package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.util;

import java.util.Random;
//purpose :- generate barcode number for non branded product
public class BarcodeGeneratorUtil {

    private static final Random random = new Random();

    public static String generateBarcode() {

        String prefix = "900"; // custom shop prefix
        long timestamp = System.currentTimeMillis();

        int randomDigits = 100 + random.nextInt(900);

        return prefix + timestamp + randomDigits;
    }
}
package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.printer;

import com.QuipBill_server.QuipBill.common.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

@Service
public class BarcodePrinterService {

    //purpose :- print barcode label 
    public void printBarcode(String labelPath) {

        try {

            File file = new File(labelPath);

            if(!file.exists()) {
                throw new ApiException(HttpStatus.NOT_FOUND, "Label file not found");
            }

            if(Desktop.isDesktopSupported()) {
                Desktop.getDesktop().print(file);
            }

        } catch (IOException e) {
            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to print barcode");
        }
    }
}

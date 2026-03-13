package com.QuipBill_server.QuipBill.modules.shopOwner.inventory.service;

import com.QuipBill_server.QuipBill.common.exception.ApiException;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.client.j2se.MatrixToImageWriter;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

@Service
public class BarcodeLabelService {

    private static final String LABEL_FOLDER = "labels/";

    public String generateLabel(String barcode) {

        try {

            File folder = new File(LABEL_FOLDER);
            if(!folder.exists()) {
                folder.mkdirs();
            }

            Code128Writer barcodeWriter = new Code128Writer();

            BitMatrix bitMatrix = barcodeWriter.encode(
                    barcode,
                    BarcodeFormat.CODE_128,
                    300,
                    120
            );

            String filePath = LABEL_FOLDER + barcode + ".png";

            Path path = new File(filePath).toPath();

            MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);

            return "/labels/" + barcode + ".png";

        } catch (IOException e) {

            throw new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to generate barcode label");
        }
    }
}

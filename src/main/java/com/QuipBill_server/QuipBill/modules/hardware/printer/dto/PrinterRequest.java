package com.QuipBill_server.QuipBill.modules.hardware.printer.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrinterRequest {

    private String printerName;

    private String printerType; // USB / NETWORK / BLUETOOTH

}
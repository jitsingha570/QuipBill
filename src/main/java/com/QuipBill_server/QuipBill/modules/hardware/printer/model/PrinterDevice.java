package com.QuipBill_server.QuipBill.modules.hardware.printer.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrinterDevice {

    private String printerId;

    private String printerName;

    private String printerType; // USB / NETWORK / BLUETOOTH

    private String ipAddress;

    private Integer port;

    private boolean connected;

}
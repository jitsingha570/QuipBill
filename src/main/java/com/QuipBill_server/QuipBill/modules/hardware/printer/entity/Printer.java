package com.QuipBill_server.QuipBill.modules.hardware.printer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Printer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long shopId;

    private String printerName;

    private String printerType;

    private String ipAddress;

    private Integer port;

    private Boolean defaultPrinter;

}
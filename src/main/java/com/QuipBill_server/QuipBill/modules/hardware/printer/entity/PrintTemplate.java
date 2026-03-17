package com.QuipBill_server.QuipBill.modules.hardware.printer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrintTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long shopId;

    private String paperSize; // 58mm / 80mm

    private Boolean showLogo;

    private Boolean showGst;

    private String footerText;

}
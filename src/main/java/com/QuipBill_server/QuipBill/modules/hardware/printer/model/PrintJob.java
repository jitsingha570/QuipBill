package com.QuipBill_server.QuipBill.modules.hardware.printer.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrintJob {

    private String jobId;

    private String printerId;

    private String jobType; // RECEIPT / LABEL

    private String content;

    private String status; // PENDING / PRINTED / FAILED

    private LocalDateTime createdAt;

}
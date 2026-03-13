package com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Last7DaysSalesResponse {

    private LocalDate date;
    private Double totalSales;
}
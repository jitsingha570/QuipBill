package com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonthlySalesResponse {

    private String month;
    private Double totalSales;
}
package com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardSummaryResponse {

    private Double todaySales;
    private Integer todayBills;

    private Double monthlySales;
    private Integer monthlyBills;

    private Double yearlySales;
    private Integer yearlyBills;

    private Double yearlyGstAmount;
    private Integer yearlyGstBills;
}

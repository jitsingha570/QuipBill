package com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.service;

import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto.DashboardSummaryResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto.Last7DaysSalesResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto.MonthlySalesResponse;

import java.util.List;

public interface DashboardService {

    DashboardSummaryResponse getSummary(Long shopId);

    List<Last7DaysSalesResponse> getLast7DaysSales(Long shopId);

    List<MonthlySalesResponse> getYearlySales(Long shopId);
}
package com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.controller;

import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto.DashboardSummaryResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto.Last7DaysSalesResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto.MonthlySalesResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.service.DashboardService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;


    // 1️⃣ Dashboard Summary (today, month, year)
    @GetMapping("/summary/{shopId}")
    public DashboardSummaryResponse getDashboardSummary(
            @PathVariable Long shopId) {

        return dashboardService.getSummary(shopId);
    }


    // 2️⃣ Last 7 days sales (Bar Chart)
    @GetMapping("/sales/last-7-days/{shopId}")
    public List<Last7DaysSalesResponse> getLast7DaysSales(
            @PathVariable Long shopId) {

        return dashboardService.getLast7DaysSales(shopId);
    }


    // 3️⃣ Yearly sales (12 month graph)
    @GetMapping("/sales/year/{shopId}")
    public List<MonthlySalesResponse> getYearlySales(
            @PathVariable Long shopId) {

        return dashboardService.getYearlySales(shopId);
    }
}
package com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.controller;

import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto.DashboardUnlockRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto.DashboardUnlockResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.service.DashboardUnlockService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardUnlockController {

    private final DashboardUnlockService dashboardUnlockService;


    // Dashboard Unlock API
    @PostMapping("/unlock")
    public DashboardUnlockResponse unlockDashboard(
            @RequestBody DashboardUnlockRequest request) {

        return dashboardUnlockService.unlock(request);
    }
}
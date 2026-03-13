package com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DashboardUnlockRequest {

    private Long shopId;
    private String pin;
}
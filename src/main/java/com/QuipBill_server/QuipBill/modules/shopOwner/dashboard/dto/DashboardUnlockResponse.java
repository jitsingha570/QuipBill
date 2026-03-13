package com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardUnlockResponse {

    private boolean unlocked;
    private String message;
}
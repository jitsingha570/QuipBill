package com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.service;

import com.QuipBill_server.QuipBill.common.exception.ApiException;
import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto.DashboardUnlockRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto.DashboardUnlockResponse;
import com.QuipBill_server.QuipBill.modules.authentication.repository.ShopRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardUnlockService {

    private final ShopRepository shopRepository;


    public DashboardUnlockResponse unlock(DashboardUnlockRequest request) {

        var shop = shopRepository.findById(request.getShopId())
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Shop not found"));

        if (shop.getDashboardPin().equals(request.getPin())) {

            return DashboardUnlockResponse.builder()
                    .unlocked(true)
                    .message("Dashboard unlocked successfully")
                    .build();
        }

        return DashboardUnlockResponse.builder()
                .unlocked(false)
                .message("Invalid PIN")
                .build();
    }
}

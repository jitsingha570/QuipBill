package com.QuipBill_server.QuipBill.modules.shopOwner.billing.service;

import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillRequest;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.BillResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.billing.dto.PrintableBillResponse;

import java.time.LocalDate;
import java.util.List;

public interface BillingService {

    BillResponse generateBill(BillRequest request, Long shopId);

    PrintableBillResponse printBill(Long billId, Long shopId);

    BillResponse getBillById(Long billId, Long shopId);

    List<BillResponse> getBillsByDate(LocalDate date, Long shopId);
}

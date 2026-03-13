package com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.service;

import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto.DashboardSummaryResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto.Last7DaysSalesResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.dto.MonthlySalesResponse;
import com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.repository.DashboardRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepository dashboardRepository;


    // 1️⃣ Dashboard Summary
    @Override
    public DashboardSummaryResponse getSummary(Long shopId) {

        LocalDate today = LocalDate.now();

        Double todaySales = dashboardRepository.getTodaySales(shopId, today);
        Integer todayBills = dashboardRepository.getTodayBills(shopId, today);

        Double monthlySales = dashboardRepository.getMonthlySales(
                shopId, today.getYear(), today.getMonthValue());

        Integer monthlyBills = dashboardRepository.getMonthlyBills(
                shopId, today.getYear(), today.getMonthValue());

        Double yearlySales = dashboardRepository.getYearlySales(
                shopId, today.getYear());

        Integer yearlyBills = dashboardRepository.getYearlyBills(
                shopId, today.getYear());

        return DashboardSummaryResponse.builder()
                .todaySales(todaySales == null ? 0 : todaySales)
                .todayBills(todayBills == null ? 0 : todayBills)
                .monthlySales(monthlySales == null ? 0 : monthlySales)
                .monthlyBills(monthlyBills == null ? 0 : monthlyBills)
                .yearlySales(yearlySales == null ? 0 : yearlySales)
                .yearlyBills(yearlyBills == null ? 0 : yearlyBills)
                .build();
    }


    // 2️⃣ Last 7 Days Sales (Bar Chart)
    @Override
    public List<Last7DaysSalesResponse> getLast7DaysSales(Long shopId) {

        List<Last7DaysSalesResponse> result = new ArrayList<>();

        for (int i = 6; i >= 0; i--) {

            LocalDate date = LocalDate.now().minusDays(i);

            Double sales = dashboardRepository.getTodaySales(shopId, date);

            Last7DaysSalesResponse res = new Last7DaysSalesResponse();

            res.setDate(date);
            res.setTotalSales(sales == null ? 0 : sales);

            result.add(res);
        }

        return result;
    }


    // 3️⃣ Yearly Sales (12 Month Graph)
    @Override
    public List<MonthlySalesResponse> getYearlySales(Long shopId) {

        List<MonthlySalesResponse> result = new ArrayList<>();

        YearMonth end = YearMonth.now(); // include current month
        YearMonth start = end.minusMonths(11); // rolling 12 months

        for (int i = 0; i < 12; i++) {
            YearMonth ym = start.plusMonths(i);
            int year = ym.getYear();
            int month = ym.getMonthValue();

            Double sales = dashboardRepository.getMonthlySales(shopId, year, month);

            MonthlySalesResponse res = new MonthlySalesResponse();
            res.setMonth(Month.of(month).name());
            res.setTotalSales(sales == null ? 0 : sales);
            result.add(res);
        }

        return result;
    }
}

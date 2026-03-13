package com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.repository;

import com.QuipBill_server.QuipBill.modules.shopOwner.billing.entity.Bill;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalesAnalyticsRepository extends JpaRepository<Bill, Long> {


    // 1️⃣ Last 7 days sales for bar chart
    @Query(value = """
        SELECT DATE(created_at) as date,
               SUM(final_amount) as totalSales
        FROM bills
        WHERE shop_id = :shopId
        AND created_at >= CURRENT_DATE - INTERVAL '7 days'
        GROUP BY DATE(created_at)
        ORDER BY DATE(created_at)
    """, nativeQuery = true)
    List<Object[]> getLast7DaysSales(@Param("shopId") Long shopId);



    // 2️⃣ Monthly sales for yearly graph
    @Query(value = """
        SELECT EXTRACT(MONTH FROM created_at) as month,
               SUM(final_amount) as totalSales
        FROM bills
        WHERE shop_id = :shopId
        AND EXTRACT(YEAR FROM created_at) = :year
        GROUP BY month
        ORDER BY month
    """, nativeQuery = true)
    List<Object[]> getYearlyMonthlySales(@Param("shopId") Long shopId,
                                         @Param("year") int year);
}
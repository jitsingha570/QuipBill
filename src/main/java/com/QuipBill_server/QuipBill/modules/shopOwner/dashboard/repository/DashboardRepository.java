package com.QuipBill_server.QuipBill.modules.shopOwner.dashboard.repository;

import com.QuipBill_server.QuipBill.modules.shopOwner.billing.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface DashboardRepository extends JpaRepository<Bill, Long> {


    // 1️⃣ Today total sales
    @Query("""
        SELECT SUM(b.finalAmount)
        FROM Bill b
        WHERE b.shopId = :shopId
        AND DATE(b.createdAt) = :date
    """)
    Double getTodaySales(@Param("shopId") Long shopId,
                         @Param("date") LocalDate date);


    // 2️⃣ Today bill count
    @Query("""
        SELECT COUNT(b)
        FROM Bill b
        WHERE b.shopId = :shopId
        AND DATE(b.createdAt) = :date
    """)
    Integer getTodayBills(@Param("shopId") Long shopId,
                          @Param("date") LocalDate date);


    // 3️⃣ Monthly sales
    @Query("""
        SELECT SUM(b.finalAmount)
        FROM Bill b
        WHERE b.shopId = :shopId
        AND YEAR(b.createdAt) = :year
        AND MONTH(b.createdAt) = :month
    """)
    Double getMonthlySales(@Param("shopId") Long shopId,
                           @Param("year") int year,
                           @Param("month") int month);


    // 4️⃣ Monthly bill count
    @Query("""
        SELECT COUNT(b)
        FROM Bill b
        WHERE b.shopId = :shopId
        AND YEAR(b.createdAt) = :year
        AND MONTH(b.createdAt) = :month
    """)
    Integer getMonthlyBills(@Param("shopId") Long shopId,
                            @Param("year") int year,
                            @Param("month") int month);


    // 5️⃣ Yearly sales
    @Query("""
        SELECT SUM(b.finalAmount)
        FROM Bill b
        WHERE b.shopId = :shopId
        AND YEAR(b.createdAt) = :year
    """)
    Double getYearlySales(@Param("shopId") Long shopId,
                          @Param("year") int year);


    // 6️⃣ Yearly bill count
    @Query("""
        SELECT COUNT(b)
        FROM Bill b
        WHERE b.shopId = :shopId
        AND YEAR(b.createdAt) = :year
    """)
    Integer getYearlyBills(@Param("shopId") Long shopId,
                           @Param("year") int year);


    // 7️⃣ Yearly GST total amount
    @Query("""
        SELECT SUM(bi.gstAmount)
        FROM BillItem bi
        JOIN bi.bill b
        WHERE b.shopId = :shopId
        AND YEAR(b.createdAt) = :year
        AND bi.gstEnabled = true
    """)
    Double getYearlyGstAmount(@Param("shopId") Long shopId,
                              @Param("year") int year);


    // 8️⃣ Yearly bill count with GST
    @Query("""
        SELECT COUNT(DISTINCT b)
        FROM BillItem bi
        JOIN bi.bill b
        WHERE b.shopId = :shopId
        AND YEAR(b.createdAt) = :year
        AND bi.gstEnabled = true
    """)
    Integer getYearlyGstBills(@Param("shopId") Long shopId,
                              @Param("year") int year);
}

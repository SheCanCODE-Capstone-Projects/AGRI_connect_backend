package com.scc.Agriconnect.repository;

import com.scc.Agriconnect.entity.Sale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {

    Page<Sale> findByCooperative_CooperativeId(Long cooperativeId, Pageable pageable);

    List<Sale> findByCustomer_CustomerId(Long customerId);

    Page<Sale> findByProduct_ProductId(Long productId, Pageable pageable);

    List<Sale> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);

    Page<Sale> findByCooperative_CooperativeIdAndSaleDateBetween(
            Long cooperativeId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s " +
           "WHERE s.cooperative.cooperativeId = :cooperativeId")
    BigDecimal getTotalRevenue(@Param("cooperativeId") Long cooperativeId);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s " +
           "WHERE s.cooperative.cooperativeId = :cooperativeId " +
           "AND s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal getRevenueBetween(@Param("cooperativeId") Long cooperativeId,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(s.quantitySold), 0) FROM Sale s " +
           "WHERE s.product.productId = :productId")
    BigDecimal getTotalQuantitySold(@Param("productId") Long productId);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s " +
           "WHERE s.product.productId = :productId")
    BigDecimal getRevenueByProduct(@Param("productId") Long productId);

    @Query("SELECT s.product.productId, s.product.name, SUM(s.quantitySold) as totalQty, " +
           "SUM(s.totalAmount) as totalRevenue " +
           "FROM Sale s " +
           "WHERE s.cooperative.cooperativeId = :cooperativeId " +
           "GROUP BY s.product.productId, s.product.name " +
           "ORDER BY totalQty DESC")
    List<Object[]> findTopSellingProducts(@Param("cooperativeId") Long cooperativeId);

    @Query("SELECT s.saleDate, COUNT(s) as totalSales, SUM(s.totalAmount) as totalRevenue " +
           "FROM Sale s " +
           "WHERE s.cooperative.cooperativeId = :cooperativeId " +
           "AND s.saleDate BETWEEN :startDate AND :endDate " +
           "GROUP BY s.saleDate " +
           "ORDER BY s.saleDate DESC")
    List<Object[]> getDailySalesSummary(@Param("cooperativeId") Long cooperativeId,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    @Query("SELECT YEAR(s.saleDate) as year, MONTH(s.saleDate) as month, " +
           "COUNT(s) as totalSales, SUM(s.totalAmount) as totalRevenue " +
           "FROM Sale s " +
           "WHERE s.cooperative.cooperativeId = :cooperativeId " +
           "GROUP BY YEAR(s.saleDate), MONTH(s.saleDate) " +
           "ORDER BY YEAR(s.saleDate) DESC, MONTH(s.saleDate) DESC")
    List<Object[]> getMonthlySalesSummary(@Param("cooperativeId") Long cooperativeId);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.cooperative.cooperativeId = :cooperativeId")
    Long countByCooperativeId(@Param("cooperativeId") Long cooperativeId);

    @Query("SELECT COUNT(s) FROM Sale s " +
           "WHERE s.cooperative.cooperativeId = :cooperativeId " +
           "AND s.saleDate BETWEEN :startDate AND :endDate")
    Long countByCooperativeIdAndDateRange(@Param("cooperativeId") Long cooperativeId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);
}
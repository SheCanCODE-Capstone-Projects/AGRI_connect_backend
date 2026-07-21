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
import java.util.UUID;

@Repository
public interface SaleRepository extends JpaRepository<Sale, UUID> {

    Page<Sale> findByCooperative_CooperativeId(UUID cooperativeId, Pageable pageable);

    List<Sale> findByCustomer_CustomerId(UUID customerId);

    Page<Sale> findByProduct_ProductId(UUID productId, Pageable pageable);

    List<Sale> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);

    Page<Sale> findByCooperative_CooperativeIdAndSaleDateBetween(
            UUID cooperativeId,
            LocalDate startDate,
            LocalDate endDate,
            Pageable pageable);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s " +
           "WHERE s.cooperative.cooperativeId = :cooperativeId")
    BigDecimal getTotalRevenue(@Param("cooperativeId") UUID cooperativeId);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s " +
           "WHERE s.cooperative.cooperativeId = :cooperativeId " +
           "AND s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal getRevenueBetween(@Param("cooperativeId") UUID cooperativeId,
                                 @Param("startDate") LocalDate startDate,
                                 @Param("endDate") LocalDate endDate);

    @Query("SELECT COALESCE(SUM(s.quantitySold), 0) FROM Sale s " +
           "WHERE s.product.productId = :productId")
    BigDecimal getTotalQuantitySold(@Param("productId") UUID productId);

    @Query("SELECT COALESCE(SUM(s.totalAmount), 0) FROM Sale s " +
           "WHERE s.product.productId = :productId")
    BigDecimal getRevenueByProduct(@Param("productId") UUID productId);

    @Query("SELECT s.product.productId, s.product.name, SUM(s.quantitySold) as totalQty, " +
           "SUM(s.totalAmount) as totalRevenue " +
           "FROM Sale s " +
           "WHERE s.cooperative.cooperativeId = :cooperativeId " +
           "GROUP BY s.product.productId, s.product.name " +
           "ORDER BY totalQty DESC")
    List<Object[]> findTopSellingProducts(@Param("cooperativeId") UUID cooperativeId);

    @Query("SELECT s.saleDate, COUNT(s) as totalSales, SUM(s.totalAmount) as totalRevenue " +
           "FROM Sale s " +
           "WHERE s.cooperative.cooperativeId = :cooperativeId " +
           "AND s.saleDate BETWEEN :startDate AND :endDate " +
           "GROUP BY s.saleDate " +
           "ORDER BY s.saleDate DESC")
    List<Object[]> getDailySalesSummary(@Param("cooperativeId") UUID cooperativeId,
                                        @Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate);

    @Query("SELECT YEAR(s.saleDate) as year, MONTH(s.saleDate) as month, " +
           "COUNT(s) as totalSales, SUM(s.totalAmount) as totalRevenue " +
           "FROM Sale s " +
           "WHERE s.cooperative.cooperativeId = :cooperativeId " +
           "GROUP BY YEAR(s.saleDate), MONTH(s.saleDate) " +
           "ORDER BY YEAR(s.saleDate) DESC, MONTH(s.saleDate) DESC")
    List<Object[]> getMonthlySalesSummary(@Param("cooperativeId") UUID cooperativeId);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.cooperative.cooperativeId = :cooperativeId")
    UUID countByCooperativeId(@Param("cooperativeId") UUID cooperativeId);

    @Query("SELECT COUNT(s) FROM Sale s " +
           "WHERE s.cooperative.cooperativeId = :cooperativeId " +
           "AND s.saleDate BETWEEN :startDate AND :endDate")
    UUID countByCooperativeIdAndDateRange(@Param("cooperativeId") UUID cooperativeId,
                                          @Param("startDate") LocalDate startDate,
                                          @Param("endDate") LocalDate endDate);
}
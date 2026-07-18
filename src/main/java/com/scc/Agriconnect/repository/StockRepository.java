package com.scc.Agriconnect.repository;

import com.scc.Agriconnect.entity.Stock;
import com.scc.Agriconnect.entity.StockType;
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
public interface StockRepository extends JpaRepository<Stock, Long> {

    Page<Stock> findByProduct_ProductId(Long productId, Pageable pageable);

    Page<Stock> findByProduct_Cooperative_CooperativeId(Long cooperativeId, Pageable pageable);

    List<Stock> findByProduct_ProductIdAndStockType(Long productId, StockType stockType);

    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Stock s " +
           "WHERE s.product.productId = :productId " +
           "AND s.stockType = 'IN'")
    BigDecimal getTotalStockIn(@Param("productId") Long productId);

    @Query("SELECT COALESCE(SUM(s.quantity), 0) FROM Stock s " +
           "WHERE s.product.productId = :productId " +
           "AND s.stockType = 'OUT'")
    BigDecimal getTotalStockOut(@Param("productId") Long productId);

    List<Stock> findByProduct_ProductIdAndStockDateBetween(
            Long productId,
            LocalDate startDate,
            LocalDate endDate);

    @Query("SELECT s.stockType, SUM(s.quantity) FROM Stock s " +
           "WHERE s.product.cooperative.cooperativeId = :cooperativeId " +
           "AND s.stockDate BETWEEN :startDate AND :endDate " +
           "GROUP BY s.stockType")
    List<Object[]> getStockSummaryByDateRange(
            @Param("cooperativeId") Long cooperativeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    List<Stock> findBySale_SaleId(Long saleId);

    boolean existsByProduct_ProductId(Long productId);
}
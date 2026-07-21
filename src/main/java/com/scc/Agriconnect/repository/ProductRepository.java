package com.scc.Agriconnect.repository;

import com.scc.Agriconnect.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCooperative_CooperativeIdAndStatus(Long cooperativeId, Product.ProductStatus status);
    List<Product> findByCooperative_CooperativeId(Long cooperativeId);

    // Row-locks the product for the rest of the transaction so concurrent stock
    // movements on the same product serialize instead of racing on currentStockLevel.
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.productId = :productId")
    Optional<Product> findByIdForUpdate(@Param("productId") Long productId);
}
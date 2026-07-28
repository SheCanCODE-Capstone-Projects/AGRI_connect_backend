package com.scc.Agriconnect.repository;

import com.scc.Agriconnect.entity.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByCooperative_CooperativeIdAndStatus(UUID cooperativeId, Product.ProductStatus status);
    List<Product> findByCooperative_CooperativeId(UUID cooperativeId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Product p where p.productId = :productId")
    Optional<Product> findByIdForUpdate(@Param("productId") UUID productId);

    @Query("select p from Product p where p.cooperative.cooperativeId = :cooperativeId " +
           "and p.reorderThreshold is not null and p.currentStockLevel <= p.reorderThreshold")
    List<Product> findLowStock(@Param("cooperativeId") UUID cooperativeId);

    @Query("select p from Product p where p.status = com.scc.Agriconnect.entity.Product.ProductStatus.VISIBLE " +
           "and p.currentStockLevel > 0 " +
           "and p.cooperative.status = com.scc.Agriconnect.entity.Cooperative.CooperativeStatus.APPROVED " +
           "order by p.name asc")
    List<Product> findAllPublicProducts();

    @Query("select p from Product p where p.cooperative.cooperativeId = :cooperativeId " +
           "and p.status = com.scc.Agriconnect.entity.Product.ProductStatus.VISIBLE " +
           "and p.currentStockLevel > 0 " +
           "and p.cooperative.status = com.scc.Agriconnect.entity.Cooperative.CooperativeStatus.APPROVED " +
           "order by p.name asc")
    List<Product> findPublicProductsByCooperative(@Param("cooperativeId") UUID cooperativeId);
}
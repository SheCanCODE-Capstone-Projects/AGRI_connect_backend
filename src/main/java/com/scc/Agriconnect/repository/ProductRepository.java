package com.scc.Agriconnect.repository;

import com.scc.Agriconnect.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCooperative_CooperativeIdAndStatus(Long cooperativeId, Product.ProductStatus status);
    List<Product> findByCooperative_CooperativeId(Long cooperativeId);
}
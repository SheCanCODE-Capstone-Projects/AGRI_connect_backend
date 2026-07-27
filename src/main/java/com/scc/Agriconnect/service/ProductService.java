package com.scc.Agriconnect.service;

import com.scc.Agriconnect.Exception.ConflictException;
import com.scc.Agriconnect.dto.ProductRequest;
import com.scc.Agriconnect.entity.Cooperative;
import com.scc.Agriconnect.entity.Product;
import com.scc.Agriconnect.entity.User;
import com.scc.Agriconnect.repository.ProductRepository;
import com.scc.Agriconnect.repository.StockRepository;
import com.scc.Agriconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final StockRepository stockRepository;

    @Transactional
    public Product create(ProductRequest request) {
        Cooperative cooperative = getCurrentUserCooperative();

        Product product = Product.builder()
                .cooperative(cooperative)
                .name(request.getName())
                .category(request.getCategory())
                .description(request.getDescription())
                .unitPrice(request.getUnitPrice())
                .unit(request.getUnit())
                .reorderThreshold(request.getReorderThreshold())
                .storageLocation(request.getStorageLocation())
                .dateReceived(request.getDateReceived())
                .imageUrl(request.getImageUrl())
                .currentStockLevel(BigDecimal.ZERO)
                .status(Product.ProductStatus.VISIBLE)
                .build();

        return productRepository.save(product);
    }

    @Transactional
    public Product update(UUID productId, ProductRequest request) {
        Product product = getOwnedProductOrThrow(productId);

        product.setName(request.getName());
        product.setCategory(request.getCategory());
        product.setDescription(request.getDescription());
        product.setUnitPrice(request.getUnitPrice());
        product.setUnit(request.getUnit());
        product.setReorderThreshold(request.getReorderThreshold());
        product.setStorageLocation(request.getStorageLocation());
        product.setDateReceived(request.getDateReceived());
        product.setImageUrl(request.getImageUrl());

        return productRepository.save(product);
    }

    @Transactional
    public Product setVisibility(UUID productId, boolean visible) {
        Product product = getOwnedProductOrThrow(productId);
        product.setStatus(visible ? Product.ProductStatus.VISIBLE : Product.ProductStatus.HIDDEN);
        return productRepository.save(product);
    }

    public List<Product> listForCurrentCooperative() {
        Cooperative cooperative = getCurrentUserCooperative();
        return productRepository.findByCooperative_CooperativeId(cooperative.getCooperativeId());
    }

    public List<Product> listLowStockForCurrentCooperative() {
        Cooperative cooperative = getCurrentUserCooperative();
        return productRepository.findLowStock(cooperative.getCooperativeId());
    }

    public List<Product> listPublicProductsByCooperative(UUID cooperativeId) {
        return productRepository.findByCooperative_CooperativeIdAndStatus(cooperativeId, Product.ProductStatus.VISIBLE);
    }

    @Transactional
    public void delete(UUID productId) {
        Product product = getOwnedProductOrThrow(productId);
        if (stockRepository.existsByProduct_ProductId(productId)) {
            throw new ConflictException("Product has stock history and cannot be deleted");
        }
        productRepository.delete(product);
    }

    private Product getOwnedProductOrThrow(UUID productId) {
        Cooperative cooperative = getCurrentUserCooperative();
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));

        if (!product.getCooperative().getCooperativeId().equals(cooperative.getCooperativeId())) {
            throw new IllegalStateException("You do not have access to this product");
        }
        return product;
    }

    private Cooperative getCurrentUserCooperative() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User freshUser = userRepository.findById(currentUser.getUserId())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));

        Cooperative cooperative = freshUser.getCooperative();
        if (cooperative == null) {
            throw new IllegalStateException("Only cooperative members can manage products");
        }
        if (cooperative.getStatus() != Cooperative.CooperativeStatus.APPROVED) {
            throw new IllegalStateException("Your cooperative is not yet approved");
        }
        return cooperative;
    }
}
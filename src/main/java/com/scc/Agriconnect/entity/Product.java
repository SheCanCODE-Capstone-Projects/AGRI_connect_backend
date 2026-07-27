package com.scc.Agriconnect.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id")
    private UUID productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cooperative_id", nullable = false)
    private Cooperative cooperative;

    @Column(nullable = false)
    private String name;

    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal unitPrice;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductUnit unit;

    @Column(name = "storage_location")
    private String storageLocation;

    @Column(name = "date_received")
    private LocalDate dateReceived;

    @Column(name = "current_stock_level", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal currentStockLevel = BigDecimal.ZERO;

    @Column(name = "reorder_threshold", precision = 12, scale = 2)
    private BigDecimal reorderThreshold;

    @Column(name = "image_url")
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private ProductStatus status = ProductStatus.VISIBLE;

    public enum ProductStatus {
        VISIBLE, HIDDEN
    }
}
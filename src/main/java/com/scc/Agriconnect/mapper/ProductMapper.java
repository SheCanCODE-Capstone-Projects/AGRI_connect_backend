package com.scc.Agriconnect.mapper;

import com.scc.Agriconnect.dto.ProductResponse;
import com.scc.Agriconnect.dto.PublicProductResponse;
import com.scc.Agriconnect.entity.Product;

public class ProductMapper {

    public static ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .productId(product.getProductId())
                .cooperativeId(product.getCooperative().getCooperativeId())
                .name(product.getName())
                .category(product.getCategory())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .unit(product.getUnit())
                .storageLocation(product.getStorageLocation())
                .dateReceived(product.getDateReceived())
                .currentStockLevel(product.getCurrentStockLevel())
                .reorderThreshold(product.getReorderThreshold())
                .imageUrl(product.getImageUrl())
                .status(product.getStatus().name())
                .build();
    }

    public static PublicProductResponse toPublicResponse(Product product) {
        return PublicProductResponse.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .category(product.getCategory())
                .description(product.getDescription())
                .unitPrice(product.getUnitPrice())
                .unit(product.getUnit())
                .imageUrl(product.getImageUrl())
                .cooperativeId(product.getCooperative().getCooperativeId())
                .cooperativeName(product.getCooperative().getName())
                .province(product.getCooperative().getProvince())
                .district(product.getCooperative().getDistrict())
                .sector(product.getCooperative().getSector())
                .contactInfo(product.getCooperative().getContactInfo())
                .build();
    }
}
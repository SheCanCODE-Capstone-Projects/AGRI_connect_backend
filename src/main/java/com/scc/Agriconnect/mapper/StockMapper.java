package com.scc.Agriconnect.mapper;

import com.scc.Agriconnect.dto.StockRequest;
import com.scc.Agriconnect.dto.StockResponse;
import com.scc.Agriconnect.entity.Stock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StockMapper {

    StockMapper INSTANCE = Mappers.getMapper(StockMapper.class);

    @Mapping(target = "stockId", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "sale", ignore = true)
    @Mapping(target = "recordedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Stock toEntity(StockRequest request);

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.category", target = "category")
    @Mapping(source = "recordedBy.fullName", target = "recordedByFullName")
    @Mapping(source = "sale.saleId", target = "saleId")
    StockResponse toResponse(Stock stock);

    @Mapping(target = "stockId", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "sale", ignore = true)
    @Mapping(target = "recordedBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(@MappingTarget Stock stock, StockRequest request);
}
package com.scc.Agriconnect.mapper;

import com.scc.Agriconnect.dto.SaleRequest;
import com.scc.Agriconnect.dto.SaleResponse;
import com.scc.Agriconnect.entity.Sale;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SaleMapper {

    SaleMapper INSTANCE = Mappers.getMapper(SaleMapper.class);

    @Mapping(target = "saleId", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "cooperative", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "recordedBy", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "voided", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Sale toEntity(SaleRequest request);

    @Mapping(source = "product.productId", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.category", target = "category")
    @Mapping(source = "product.unit", target = "unit")
    @Mapping(source = "cooperative.cooperativeId", target = "cooperativeId")
    @Mapping(source = "cooperative.name", target = "cooperativeName")
    @Mapping(source = "customer.customerId", target = "customerId")
    @Mapping(source = "customer.fullName", target = "customerName")
    @Mapping(source = "recordedBy.fullName", target = "recordedByFullName")
    SaleResponse toResponse(Sale sale);

    @Mapping(target = "saleId", ignore = true)
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "cooperative", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "recordedBy", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "voided", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(@MappingTarget Sale sale, SaleRequest request);
}
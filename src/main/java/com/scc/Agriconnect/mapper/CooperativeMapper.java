package com.scc.Agriconnect.mapper;

import com.scc.Agriconnect.dto.CooperativeResponse;
import com.scc.Agriconnect.entity.Cooperative;

public class CooperativeMapper {
    public static CooperativeResponse toResponse(Cooperative c) {
        return CooperativeResponse.builder()
                .cooperativeId(c.getCooperativeId())
                .name(c.getName())
                .registrationNumber(c.getRegistrationNumber())
                .province(c.getProvince())
                .district(c.getDistrict())
                .sector(c.getSector())
                .status(c.getStatus().name())
                .build();
    }
}
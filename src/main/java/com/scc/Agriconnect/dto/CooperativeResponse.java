package com.scc.Agriconnect.dto;

import lombok.*;

import java.util.UUID;

@Getter @Setter @Builder
public class CooperativeResponse {
    private UUID cooperativeId;
    private String name;
    private String registrationNumber;
    private String province;
    private String district;
    private String sector;
    private String status;
}
package com.scc.Agriconnect.dto;

import lombok.*;

@Getter @Setter @Builder
public class CooperativeResponse {
    private Long cooperativeId;
    private String name;
    private String registrationNumber;
    private String province;
    private String district;
    private String sector;
    private String status;
}
package com.scc.Agriconnect.dto;

import lombok.*;

@Getter @Setter
@Builder
public class AuthResponse {
    private String accessToken;
    private String tokenType;
}
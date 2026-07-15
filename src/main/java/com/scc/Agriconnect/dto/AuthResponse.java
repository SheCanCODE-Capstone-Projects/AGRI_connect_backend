package com.scc.Agriconnect.dto;

import lombok.*;

@Getter @Setter
@Builder
public class AuthResponse {
    private String token;
    private String fullName;
    private String email;
    private String role;
    private Long cooperativeId;
    private String cooperativeStatus;
}
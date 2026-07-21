package com.scc.Agriconnect.dto;

import lombok.*;

import java.util.UUID;

@Getter @Setter @Builder
public class UserResponse {
    private UUID userId;
    private String fullName;
    private String email;
    private String role;
    private UUID cooperativeId;
}
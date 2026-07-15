package com.scc.Agriconnect.dto;

import lombok.*;

@Getter @Setter @Builder
public class UserResponse {
    private Long userId;
    private String fullName;
    private String email;
    private String role;
    private Long cooperativeId;
}
package com.scc.Agriconnect.dto;

import lombok.*;

import java.util.UUID;

@Getter @Setter @Builder
public class InvitationResponse {
    private UUID invitationId;
    private String email;
    private String role;
    private String status;
}
package com.scc.Agriconnect.dto;

import lombok.*;

@Getter @Setter @Builder
public class InvitationResponse {
    private Long invitationId;
    private String email;
    private String role;
    private String status;
}
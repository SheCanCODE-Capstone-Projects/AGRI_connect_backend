package com.scc.Agriconnect.mapper;

import com.scc.Agriconnect.dto.InvitationResponse;
import com.scc.Agriconnect.entity.StaffInvitation;

public class InvitationMapper {
    public static InvitationResponse toResponse(StaffInvitation i) {
        return InvitationResponse.builder()
                .invitationId(i.getInvitationId())
                .email(i.getEmail())
                .role(i.getRole().getName())
                .status(i.getStatus().name())
                .build();
    }
}
package com.scc.Agriconnect.mapper;

import com.scc.Agriconnect.dto.UserResponse;
import com.scc.Agriconnect.entity.User;

public class UserMapper {
    public static UserResponse toResponse(User u) {
        return UserResponse.builder()
                .userId(u.getUserId())
                .fullName(u.getFullName())
                .email(u.getEmail())
                .role(u.getRole().getName())
                .cooperativeId(u.getCooperative() != null ? u.getCooperative().getCooperativeId() : null)
                .build();
    }
}
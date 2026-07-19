package com.scc.Agriconnect.dto;

import lombok.*;

import java.time.LocalDate;

@Getter @Setter @Builder
public class MemberResponse {
    private Long memberId;
    private String fullName;
    private String phoneNumber;
    private String nationalId;
    private String address;
    private String gender;
    private LocalDate dateJoined;
    private String membershipStatus;
}
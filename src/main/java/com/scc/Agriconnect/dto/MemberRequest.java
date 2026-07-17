package com.scc.Agriconnect.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter @Setter
public class MemberRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String nationalId;

    @NotBlank
    private String address;

    @NotBlank
    private String gender;


    private LocalDate dateJoined;
}
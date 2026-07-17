package com.scc.Agriconnect.mapper;

import com.scc.Agriconnect.dto.MemberResponse;
import com.scc.Agriconnect.entity.Member;

public class MemberMapper {
    public static MemberResponse toResponse(Member member) {
        return MemberResponse.builder()
                .memberId(member.getMemberId())
                .fullName(member.getFullName())
                .phoneNumber(member.getPhoneNumber())
                .nationalId(member.getNationalId())
                .address(member.getAddress())
                .gender(member.getGender())
                .dateJoined(member.getDateJoined())
                .membershipStatus(member.getMembershipStatus().name())
                .build();
    }
}
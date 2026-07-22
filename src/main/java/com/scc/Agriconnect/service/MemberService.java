package com.scc.Agriconnect.service;

import com.scc.Agriconnect.entity.Cooperative;
import com.scc.Agriconnect.entity.Member;
import com.scc.Agriconnect.entity.User;
import com.scc.Agriconnect.dto.MemberRequest;
import com.scc.Agriconnect.repository.MemberRepository;
import io.swagger.v3.oas.models.media.UUIDSchema;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Member create(MemberRequest request) {
        Cooperative cooperative = getCurrentCooperative();

        return memberRepository.save(Member.builder()
                .cooperative(cooperative)
                .fullName(request.getFullName())
                .phoneNumber(request.getPhoneNumber())
                .nationalId(request.getNationalId())
                .address(request.getAddress())
                .gender(request.getGender())
                .dateJoined(request.getDateJoined() != null ? request.getDateJoined() : LocalDate.now())
                .build());
    }

    public List<Member> list(Member.MembershipStatus status, String name) {
        UUID cooperativeId = getCurrentCooperative().getCooperativeId();
        return memberRepository.search(cooperativeId, status, name);
    }

    public Member getOne(UUID memberId) {
        return findScopedOrThrow(memberId);
    }

    @Transactional
    public Member update(UUID memberId, MemberRequest request) {
        Member member = findScopedOrThrow(memberId);

        member.setFullName(request.getFullName());
        member.setPhoneNumber(request.getPhoneNumber());
        member.setNationalId(request.getNationalId());
        member.setAddress(request.getAddress());
        member.setGender(request.getGender());
        if (request.getDateJoined() != null) {
            member.setDateJoined(request.getDateJoined());
        }

        return memberRepository.save(member);
    }

    @Transactional
    public Member softDelete(UUID memberId) {
        Member member = findScopedOrThrow(memberId);
        member.setMembershipStatus(Member.MembershipStatus.INACTIVE);
        return memberRepository.save(member);
    }

    @Transactional
    public Member reactivate(UUID memberId) {
        Member member = findScopedOrThrow(memberId);
        member.setMembershipStatus(Member.MembershipStatus.ACTIVE);
        return memberRepository.save(member);
    }

    private Member findScopedOrThrow(UUID memberId) {
        UUID cooperativeId = getCurrentCooperative().getCooperativeId();
        return memberRepository.findByMemberIdAndCooperative_CooperativeId(memberId, cooperativeId)
                .orElseThrow(() -> new IllegalArgumentException("Member not found in your cooperative"));
    }

    private Cooperative getCurrentCooperative() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Cooperative cooperative = currentUser.getCooperative();
        if (cooperative == null) {
            throw new IllegalStateException("Only cooperative staff can manage members");
        }
        return cooperative;
    }
}
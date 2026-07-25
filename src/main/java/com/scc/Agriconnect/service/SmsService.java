package com.scc.Agriconnect.service;

import com.scc.Agriconnect.dto.SmsRequest;
import com.scc.Agriconnect.dto.SmsResponse;
import com.scc.Agriconnect.entity.Cooperative;
import com.scc.Agriconnect.entity.DeliveryStatus;
import com.scc.Agriconnect.entity.Member;
import com.scc.Agriconnect.entity.Sms;
import com.scc.Agriconnect.entity.SmsLanguage;
import com.scc.Agriconnect.entity.SmsRecipient;
import com.scc.Agriconnect.entity.User;
import com.scc.Agriconnect.integration.SmsGatewayService;
import com.scc.Agriconnect.integration.SmsSendResult;
import com.scc.Agriconnect.mapper.SmsMapper;
import com.scc.Agriconnect.repository.MemberRepository;
import com.scc.Agriconnect.repository.SmsRecipientRepository;
import com.scc.Agriconnect.repository.SmsRepository;
import com.scc.Agriconnect.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SmsService {

    private static final String GSM7_BASIC_SET =
            "@£$¥èéùìòÇ\nØø\rÅåΔ_ΦΓΛΩΠΨΣΘΞÆæßÉ !\"#¤%&'()*+,-./0123456789:;<=>?¡"
                    + "ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§¿abcdefghijklmnopqrstuvwxyzäöñüà";

    private final SmsRepository smsRepository;
    private final SmsRecipientRepository smsRecipientRepository;
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final SmsGatewayService smsGatewayService;

    @Transactional
    public SmsResponse send(SmsRequest request) {
        User user = getCurrentUser();
        Cooperative cooperative = requireCooperative(user);

        List<Member> targetMembers = resolveRecipients(cooperative, request.getMemberIds());
        if (targetMembers.isEmpty()) {
            throw new IllegalArgumentException("No active members to send to");
        }

        int characterCount = request.getMessage().length();
        int segmentCount = countSegments(request.getMessage());

        Sms sms = Sms.builder()
                .cooperative(cooperative)
                .message(request.getMessage())
                .type(request.getType())
                .language(request.getLanguage() != null ? request.getLanguage() : SmsLanguage.KINYARWANDA)
                .sentAt(LocalDateTime.now())
                .createdBy(user)
                .build();
        Sms savedSms = smsRepository.save(sms);

        List<SmsRecipient> recipients = targetMembers.stream()
                .map(member -> dispatchToMember(savedSms, member))
                .toList();

        return SmsMapper.toResponse(savedSms, recipients, characterCount, segmentCount);
    }

    public Page<SmsResponse> getCooperativeSmsHistory(Pageable pageable) {
        Cooperative cooperative = getCurrentUserCooperative();
        return smsRepository.findByCooperative_CooperativeIdOrderByCreatedAtDesc(cooperative.getCooperativeId(), pageable)
                .map(this::toResponseWithRecipients);
    }

    public SmsResponse getSmsById(UUID smsId) {
        Cooperative cooperative = getCurrentUserCooperative();
        Sms sms = smsRepository.findById(smsId)
                .orElseThrow(() -> new IllegalArgumentException("SMS not found: " + smsId));
        if (!sms.getCooperative().getCooperativeId().equals(cooperative.getCooperativeId())) {
            throw new IllegalStateException("You do not have access to this SMS");
        }
        return toResponseWithRecipients(sms);
    }

    private SmsResponse toResponseWithRecipients(Sms sms) {
        List<SmsRecipient> recipients = smsRecipientRepository.findBySms_SmsId(sms.getSmsId());
        return SmsMapper.toResponse(sms, recipients, sms.getMessage().length(), countSegments(sms.getMessage()));
    }

    private SmsRecipient dispatchToMember(Sms sms, Member member) {
        SmsSendResult result = smsGatewayService.send(member.getPhoneNumber(), sms.getMessage());

        SmsRecipient recipient = SmsRecipient.builder()
                .sms(sms)
                .member(member)
                .deliveryStatus(result.success() ? DeliveryStatus.SENT : DeliveryStatus.FAILED)
                .providerMessageId(result.providerMessageId())
                .failureReason(result.failureReason())
                .build();

        return smsRecipientRepository.save(recipient);
    }

    private List<Member> resolveRecipients(Cooperative cooperative, List<UUID> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) {
            return memberRepository.search(cooperative.getCooperativeId(), Member.MembershipStatus.ACTIVE, null);
        }

        return memberIds.stream()
                .map(memberId -> memberRepository.findByMemberIdAndCooperative_CooperativeId(memberId, cooperative.getCooperativeId())
                        .orElseThrow(() -> new IllegalArgumentException("Member not found in your cooperative: " + memberId)))
                .toList();
    }

    private int countSegments(String message) {
        boolean isGsm7 = message.chars().allMatch(c -> GSM7_BASIC_SET.indexOf(c) >= 0);
        int singleSegmentLimit = isGsm7 ? 160 : 70;
        int multiSegmentLimit = isGsm7 ? 153 : 67;

        if (message.length() <= singleSegmentLimit) {
            return 1;
        }
        return (int) Math.ceil((double) message.length() / multiSegmentLimit);
    }

    private Cooperative requireCooperative(User user) {
        Cooperative cooperative = user.getCooperative();
        if (cooperative == null) {
            throw new IllegalStateException("Only cooperative members can send SMS announcements");
        }
        if (cooperative.getStatus() != Cooperative.CooperativeStatus.APPROVED) {
            throw new IllegalStateException("Your cooperative is not yet approved");
        }
        return cooperative;
    }

    private Cooperative getCurrentUserCooperative() {
        return requireCooperative(getCurrentUser());
    }

    private User getCurrentUser() {
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }
}

package com.scc.Agriconnect.mapper;

import com.scc.Agriconnect.dto.SmsRecipientResponse;
import com.scc.Agriconnect.dto.SmsResponse;
import com.scc.Agriconnect.entity.DeliveryStatus;
import com.scc.Agriconnect.entity.Sms;
import com.scc.Agriconnect.entity.SmsRecipient;

import java.util.List;

public class SmsMapper {

    public static SmsResponse toResponse(Sms sms, List<SmsRecipient> recipients, int characterCount, int segmentCount) {
        List<SmsRecipientResponse> recipientResponses = recipients.stream()
                .map(SmsMapper::toRecipientResponse)
                .toList();

        long sentCount = recipients.stream()
                .filter(r -> r.getDeliveryStatus() == DeliveryStatus.SENT || r.getDeliveryStatus() == DeliveryStatus.DELIVERED)
                .count();
        long failedCount = recipients.stream()
                .filter(r -> r.getDeliveryStatus() == DeliveryStatus.FAILED)
                .count();

        return SmsResponse.builder()
                .smsId(sms.getSmsId())
                .cooperativeId(sms.getCooperative().getCooperativeId())
                .message(sms.getMessage())
                .type(sms.getType())
                .language(sms.getLanguage())
                .characterCount(characterCount)
                .segmentCount(segmentCount)
                .totalRecipients(recipients.size())
                .sentCount((int) sentCount)
                .failedCount((int) failedCount)
                .sentAt(sms.getSentAt())
                .createdByFullName(sms.getCreatedBy().getFullName())
                .recipients(recipientResponses)
                .build();
    }

    public static SmsRecipientResponse toRecipientResponse(SmsRecipient recipient) {
        return SmsRecipientResponse.builder()
                .memberId(recipient.getMember().getMemberId())
                .memberFullName(recipient.getMember().getFullName())
                .phoneNumber(recipient.getMember().getPhoneNumber())
                .deliveryStatus(recipient.getDeliveryStatus())
                .failureReason(recipient.getFailureReason())
                .build();
    }
}

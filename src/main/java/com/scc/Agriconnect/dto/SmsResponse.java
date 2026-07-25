package com.scc.Agriconnect.dto;

import com.scc.Agriconnect.entity.SmsLanguage;
import com.scc.Agriconnect.entity.SmsType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@Schema(description = "Response containing a sent SMS announcement and its delivery outcomes")
public class SmsResponse {

    @Schema(description = "Unique identifier of the SMS announcement")
    private UUID smsId;

    @Schema(description = "UUID of the cooperative that sent this announcement")
    private UUID cooperativeId;

    @Schema(description = "Message text")
    private String message;

    @Schema(description = "Type of announcement")
    private SmsType type;

    @Schema(description = "Language the message is written in")
    private SmsLanguage language;

    @Schema(description = "Number of characters in the message")
    private int characterCount;

    @Schema(description = "Number of SMS segments this message will be billed as")
    private int segmentCount;

    @Schema(description = "Total number of recipients")
    private int totalRecipients;

    @Schema(description = "Number of recipients the message was successfully sent to")
    private int sentCount;

    @Schema(description = "Number of recipients delivery failed for")
    private int failedCount;

    @Schema(description = "When this announcement was sent")
    private LocalDateTime sentAt;

    @Schema(description = "Full name of the staff member who sent this announcement")
    private String createdByFullName;

    @Schema(description = "Per-recipient delivery outcomes")
    private List<SmsRecipientResponse> recipients;
}

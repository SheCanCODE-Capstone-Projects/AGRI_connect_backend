package com.scc.Agriconnect.dto;

import com.scc.Agriconnect.entity.DeliveryStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@Schema(description = "Delivery outcome for a single member recipient of an SMS")
public class SmsRecipientResponse {

    @Schema(description = "Member UUID")
    private UUID memberId;

    @Schema(description = "Member full name")
    private String memberFullName;

    @Schema(description = "Member phone number")
    private String phoneNumber;

    @Schema(description = "Delivery status for this recipient")
    private DeliveryStatus deliveryStatus;

    @Schema(description = "Reason for failure, if delivery failed")
    private String failureReason;
}

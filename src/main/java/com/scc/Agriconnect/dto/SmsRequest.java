package com.scc.Agriconnect.dto;

import com.scc.Agriconnect.entity.SmsLanguage;
import com.scc.Agriconnect.entity.SmsType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "Request payload for sending an SMS announcement to cooperative members")
public class SmsRequest {

    @Schema(description = "Message text", example = "CoopAheza: Twagurishije ibiro 240 muri Nyakanga, injiza 240,000 Frw. Murakoze!", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Message is required")
    private String message;

    @Schema(description = "Type of announcement", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "Type is required")
    private SmsType type;

    @Schema(description = "Language the message is written in", example = "KINYARWANDA")
    private SmsLanguage language;

    @Schema(description = "Specific members to send to. Omit or leave empty to send to all active members.")
    private List<UUID> memberIds;
}

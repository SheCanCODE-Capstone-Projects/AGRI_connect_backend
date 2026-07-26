package com.scc.Agriconnect.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@Service
public class SmsGatewayService {

    private final RestClient restClient;
    private final String username;
    private final String apiKey;
    private final String senderId;

    public SmsGatewayService(
            @Value("${africastalking.username}") String username,
            @Value("${africastalking.api-key}") String apiKey,
            @Value("${africastalking.sender-id}") String senderId,
            @Value("${africastalking.base-url}") String baseUrl) {
        this.username = username;
        this.apiKey = apiKey;
        this.senderId = senderId;
        this.restClient = RestClient.builder().baseUrl(baseUrl).build();
    }

    public SmsSendResult send(String phoneNumber, String message) {
        if (apiKey == null || apiKey.isBlank()) {
            return SmsSendResult.failure("SMS gateway is not configured yet");
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("username", username);
        form.add("to", phoneNumber);
        form.add("message", message);
        if (senderId != null && !senderId.isBlank()) {
            form.add("from", senderId);
        }

        try {
            AfricasTalkingResponse response = restClient.post()
                    .uri("/version1/messaging")
                    .header("apiKey", apiKey)
                    .header("Accept", "application/json")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(form)
                    .retrieve()
                    .body(AfricasTalkingResponse.class);

            if (response == null || response.smsMessageData() == null
                    || response.smsMessageData().recipients() == null
                    || response.smsMessageData().recipients().isEmpty()) {
                return SmsSendResult.failure("Empty response from SMS gateway");
            }

            AfricasTalkingRecipient recipient = response.smsMessageData().recipients().get(0);
            if ("Success".equalsIgnoreCase(recipient.status())) {
                return SmsSendResult.success(recipient.messageId());
            }
            return SmsSendResult.failure(recipient.status());
        } catch (Exception ex) {
            log.error("Failed to send SMS to {}", phoneNumber, ex);
            return SmsSendResult.failure(ex.getMessage());
        }
    }

    private record AfricasTalkingResponse(@JsonProperty("SMSMessageData") SmsMessageData smsMessageData) {
    }

    private record SmsMessageData(
            @JsonProperty("Message") String message,
            @JsonProperty("Recipients") List<AfricasTalkingRecipient> recipients) {
    }

    private record AfricasTalkingRecipient(
            @JsonProperty("number") String number,
            @JsonProperty("status") String status,
            @JsonProperty("messageId") String messageId) {
    }
}

package com.scc.Agriconnect.integration;

public record SmsSendResult(boolean success, String providerMessageId, String failureReason) {

    public static SmsSendResult success(String providerMessageId) {
        return new SmsSendResult(true, providerMessageId, null);
    }

    public static SmsSendResult failure(String reason) {
        return new SmsSendResult(false, null, reason);
    }
}

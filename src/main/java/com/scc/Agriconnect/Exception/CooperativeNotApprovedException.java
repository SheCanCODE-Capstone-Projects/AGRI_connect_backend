package com.scc.Agriconnect.Exception;

public class CooperativeNotApprovedException extends RuntimeException {
    public CooperativeNotApprovedException(String message) {
        super(message);
    }
}
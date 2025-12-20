package com.daleelteq.booking.exception;

public class BusinessRuleException extends RuntimeException {
    private final String businessRule;

    public BusinessRuleException(String businessRule, String message) {
        super(message);
        this.businessRule = businessRule;
    }

    public String getBusinessRule() {
        return businessRule;
    }
}


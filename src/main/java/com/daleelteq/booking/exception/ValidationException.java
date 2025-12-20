package com.daleelteq.booking.exception;

public class ValidationException extends RuntimeException {
    private final String fieldName;
    private final String validationRule;

    public ValidationException(String fieldName, String validationRule, String message) {
        super(message);
        this.fieldName = fieldName;
        this.validationRule = validationRule;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getValidationRule() {
        return validationRule;
    }
}


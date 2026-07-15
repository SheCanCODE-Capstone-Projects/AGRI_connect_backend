package com.scc.Agriconnect.dto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator
        implements ConstraintValidator<PasswordMatches, PasswordConfirmable> {

    @Override
    public boolean isValid(PasswordConfirmable value, ConstraintValidatorContext context) {
        if (value.getPassword() == null || value.getConfirmPassword() == null) return true;
        return value.getPassword().equals(value.getConfirmPassword());
    }
}
package com.scc.Agriconnect.dto;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
public @interface PasswordMatches {
    String message() default "Password and confirm password do not match";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
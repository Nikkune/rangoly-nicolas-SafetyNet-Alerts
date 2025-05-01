package dev.nikkune.safetynet.alerts.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    private static final String PHONE_PATTERN = "\\d{3}-\\d{3}-\\d{4}";

    @Override
    public void initialize(Phone constraintAnnotation) {
        // No initialization required
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.matches(PHONE_PATTERN);
    }
}

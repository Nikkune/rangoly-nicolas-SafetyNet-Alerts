package dev.nikkune.safetynet.alerts.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = PhoneValidator.class)
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
@Retention(RUNTIME)
public @interface Phone {
    String message() default "Invalid phone number. Expected xxx-xxx-xxxx";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

package dev.nikkune.safetynet.alerts.validation;

import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class PhoneValidatorTest {
    private PhoneValidator phoneValidator;
    private ConstraintValidatorContext context;

    @BeforeEach
    public void setUp() {
        phoneValidator = new PhoneValidator();
        context = mock(ConstraintValidatorContext.class);
    }

    @Test
    public void testValidPhoneNumber() {
        // Arrange
        String validPhoneNumber = "123-456-7890";
        assertTrue(phoneValidator.isValid(validPhoneNumber, context), "Valid phone number should return true");
    }

    @Test
    public void testInvalidPhoneNumber() {
        // Arrange
        String invalidPhoneNumber = "123-4567-890";
        assertFalse(phoneValidator.isValid(invalidPhoneNumber, context), "Invalid phone number should return false");
    }

    @Test
    public void testEmptyPhoneNumber() {
        // Arrange
        String emptyPhoneNumber = "";
        // Act & Assert
        assertFalse(phoneValidator.isValid(emptyPhoneNumber, context), "Empty phone number should return false");
    }

    @Test
    public void testPhoneNumberWithLetters() {
        // Arrange
        String phoneNumberWithLetters = "123-abc-7890";
        // Act & Assert
        assertFalse(phoneValidator.isValid(phoneNumberWithLetters, context), "Phone number with letters should return false");
    }
}

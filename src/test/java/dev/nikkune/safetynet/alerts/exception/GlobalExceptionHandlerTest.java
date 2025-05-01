package dev.nikkune.safetynet.alerts.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    public void handleRuntimeException_shouldReturnNotFound_whenMessageContainsNotFound() {
        // Arrange
        RuntimeException exception = new RuntimeException("Person Not found");

        // Act
        ResponseEntity<String> response = handler.handleRuntimeException(exception);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Person Not found", response.getBody());
    }

    @Test
    public void handleRuntimeException_shouldReturnConflict_whenMessageContainsAlreadyExists() {
        // Arrange
        RuntimeException exception = new RuntimeException("Person already exists");

        // Act
        ResponseEntity<String> response = handler.handleRuntimeException(exception);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Person already exists", response.getBody());
    }

    @Test
    public void handleRuntimeException_shouldReturnBadRequest_whenMessageDoesNotContainNotFoundOrAlreadyExists() {
        // Arrange
        RuntimeException exception = new RuntimeException("Invalid input");

        // Act
        ResponseEntity<String> response = handler.handleRuntimeException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input", response.getBody());
    }

    @Test
    public void handleRuntimeException_shouldReturnBadRequest_whenMessageIsNull() {
        // Arrange
        RuntimeException exception = new RuntimeException((Throwable) null);

        // Act
        ResponseEntity<String> response = handler.handleRuntimeException(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void handleValidationExceptions_shouldReturnErrorsMap() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("person", "name", "Name is required");
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(bindingResult);

        // Act
        ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> errors = response.getBody();
        assertNotNull(errors);
        assertEquals("Name is required", errors.get("name"));
    }

    @Test
    void handleConstraintViolationExceptions_shouldReturnErrorsMap() {
        // Arrange
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);
        when(path.toString()).thenReturn("myMethodName.name");
        when(violation.getPropertyPath()).thenReturn(path);
        when(violation.getMessage()).thenReturn("Name is required");

        ConstraintViolationException exception = new ConstraintViolationException(Set.of(violation));


        // Act
        ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(exception);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, String> errors = response.getBody();
        assertNotNull(errors);
        assertEquals("Name is required", errors.get("name"));
    }

    @Test
    void handleGenericException_shouldReturnInternalServerError() {
        // Arrange
        Exception exception = new Exception("Something went wrong");

        // Act
        ResponseEntity<String> response = handler.handleGenericException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error: Something went wrong", response.getBody());
    }
}

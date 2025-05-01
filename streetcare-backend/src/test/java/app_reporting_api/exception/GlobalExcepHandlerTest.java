package app_reporting_api.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExcepHandlerTest {

    private GlobalExcepHandler globalExcepHandler;
    @BeforeEach
    void setUp() {
        globalExcepHandler = new GlobalExcepHandler();
    }

    @DisplayName("Should handle ConstraintViolationException and return 400 with error details")
    @Test
    void testHandleConstraintViolationException() {
        // Arrange
        ConstraintViolation<?> mockViolation = mock(ConstraintViolation.class);
        Path mockPath = mock(Path.class);

        when(mockPath.toString()).thenReturn("email");
        when(mockViolation.getPropertyPath()).thenReturn(mockPath);
        when(mockViolation.getMessage()).thenReturn("must be a valid email");

        Set<ConstraintViolation<?>> violations = new HashSet<>(Collections.singletonList(mockViolation));
        ConstraintViolationException exception = new ConstraintViolationException(violations);

        // Act
        ResponseEntity<Map<String, String>> response = globalExcepHandler.handleConstraintViolationException(exception);

        // Assert
        assertEquals(400, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().containsKey("email"));
        assertEquals("must be a valid email", response.getBody().get("email"));

    }
}
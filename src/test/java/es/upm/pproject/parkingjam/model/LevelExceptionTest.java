package es.upm.pproject.parkingjam.model;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class LevelExceptionTest {

    @Test
    @DisplayName("Test default constructor")
    void testDefaultConstructor() {
        LevelException exception = new LevelException();
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Test constructor with message")
    void testConstructorWithMessage() {
        String message = "Custom exception message";
        LevelException exception = new LevelException(message);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    @DisplayName("Test constructor with message and cause")
    void testConstructorWithMessageAndCause() {
        String message = "Custom exception message with cause";
        Throwable cause = new IllegalArgumentException("Cause of the exception");
        LevelException exception = new LevelException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    @DisplayName("Test constructor with cause")
    void testConstructorWithCause() {
        Throwable cause = new IllegalArgumentException("Cause of the exception");
        LevelException exception = new LevelException(cause);
        assertEquals(cause.getClass(), exception.getCause().getClass());
        assertEquals(cause.getMessage(), exception.getCause().getMessage());
    }
}

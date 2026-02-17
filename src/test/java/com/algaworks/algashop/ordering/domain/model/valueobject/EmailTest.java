package com.algaworks.algashop.ordering.domain.model.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EmailTest {

    @Test
    void shouldCreateEmailWithValidValue() {
        Email email = new Email("test@example.com");
        assertNotNull(email);
        assertEquals("test@example.com", email.value());
    }

    @Test
    void shouldThrowExceptionForNullValue() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }

    @Test
    void shouldThrowExceptionForBlankValue() {
        assertThrows(IllegalArgumentException.class, () -> new Email(""));
        assertThrows(IllegalArgumentException.class, () -> new Email(" "));
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid-email", "test@", "@domain.com", "test@domain"})
    void shouldThrowExceptionForInvalidEmailFormat(String invalidEmail) {
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        String value = "user.name+alias@domain.co.uk";
        Email email = new Email(value);
        assertEquals(value, email.toString());
    }
}

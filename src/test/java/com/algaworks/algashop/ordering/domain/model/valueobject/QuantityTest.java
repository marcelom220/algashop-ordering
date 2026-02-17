package com.algaworks.algashop.ordering.domain.model.valueobject;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuantityTest {

    @Test
    void shouldCreateQuantityWithValidValue() {
        Quantity quantity = new Quantity(10);
        assertNotNull(quantity);
        assertEquals(10, quantity.value());
    }

    @Test
    void shouldCreateQuantityWithZero() {
        Quantity quantity = new Quantity(0);
        assertNotNull(quantity);
        assertEquals(0, quantity.value());
        assertEquals(Quantity.ZERO, quantity);
    }

    @Test
    void shouldThrowExceptionForNullValue() {
        assertThrows(NullPointerException.class, () -> new Quantity(null));
    }

    @Test
    void shouldThrowExceptionForNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> new Quantity(-1));
    }

    @Test
    void shouldAddQuantities() {
        Quantity q1 = new Quantity(5);
        Quantity q2 = new Quantity(10);
        Quantity result = q1.add(q2);
        assertEquals(new Quantity(15), result);
    }

    @Test
    void shouldCompareQuantities() {
        Quantity q1 = new Quantity(5);
        Quantity q2 = new Quantity(10);
        Quantity q3 = new Quantity(5);

        assertTrue(q1.compareTo(q2) < 0);
        assertTrue(q2.compareTo(q1) > 0);
        assertEquals(0, q1.compareTo(q3));
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        Quantity quantity = new Quantity(123);
        assertEquals("123", quantity.toString());
    }
}

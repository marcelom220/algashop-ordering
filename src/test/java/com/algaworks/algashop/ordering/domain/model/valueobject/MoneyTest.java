package com.algaworks.algashop.ordering.domain.model.valueobject;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class MoneyTest {

    @Test
    void shouldCreateMoneyFromBigDecimal() {
        Money money = new Money(new BigDecimal("10.50"));
        assertNotNull(money);
        assertEquals(new BigDecimal("10.50"), money.value());
    }

    @Test
    void shouldCreateMoneyFromString() {
        Money money = new Money("25.75");
        assertNotNull(money);
        assertEquals(new BigDecimal("25.75"), money.value());
    }

    @Test
    void shouldHandleRoundingAndScaleCorrectly() {
        Money money = new Money(new BigDecimal("99.987")); // HALF_EVEN rounds down
        assertEquals(new BigDecimal("99.99"), money.value());

        Money money2 = new Money(new BigDecimal("99.995")); // HALF_EVEN rounds up
        assertEquals(new BigDecimal("100.00"), money2.value());
    }

    @Test
    void shouldThrowExceptionForNullValue() {
        assertThrows(NullPointerException.class, () -> new Money((BigDecimal) null));
        assertThrows(NullPointerException.class, () -> new Money((String) null));
    }

    @Test
    void shouldThrowExceptionForNegativeValue() {
        assertThrows(IllegalArgumentException.class, () -> new Money(new BigDecimal("-0.01")));
        assertThrows(IllegalArgumentException.class, () -> new Money("-10"));
    }

    @Test
    void shouldAddMoney() {
        Money m1 = new Money("10.50");
        Money m2 = new Money("5.25");
        Money result = m1.add(m2);
        assertEquals(new Money("15.75"), result);
    }

    @Test
    void shouldMultiplyByQuantity() {
        Money money = new Money("10.00");
        Quantity quantity = new Quantity(3);
        Money result = money.multiply(quantity);
        assertEquals(new Money("30.00"), result);
    }

    @Test
    void shouldThrowExceptionWhenMultiplyingByQuantityLessThanOne() {
        Money money = new Money("10.00");
        Quantity quantity = new Quantity(0);
        assertThrows(IllegalArgumentException.class, () -> money.multiply(quantity));
    }

    @Test
    void shouldDivideMoney() {
        Money m1 = new Money("100.00");
        Money m2 = new Money("4.00");
        Money result = m1.divide(m2);
        assertEquals(new Money("25.00"), result);
    }
    
    @Test
    void shouldThrowExceptionWhenDividingByZero() {
        Money m1 = new Money("100.00");
        assertThrows(ArithmeticException.class, () -> m1.divide(Money.ZERO));
    }

    @Test
    void shouldCompareMoney() {
        Money m1 = new Money("10.00");
        Money m2 = new Money("20.00");
        Money m3 = new Money("10.00");

        assertTrue(m1.compareTo(m2) < 0);
        assertTrue(m2.compareTo(m1) > 0);
        assertEquals(0, m1.compareTo(m3));
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        Money money = new Money("1234.56");
        assertEquals("1234.56", money.toString());
    }
}

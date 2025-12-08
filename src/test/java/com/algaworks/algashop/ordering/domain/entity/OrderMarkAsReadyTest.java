package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class OrderMarkAsReadyTest {

    @Test
    void givenPaidOrder_whenMarkAsReady_shouldChangeStatusToReadyAndSetReadyAt() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PAID).build();

        order.markAsReady();

        assertThat(order.status()).isEqualTo(OrderStatus.READY);
        assertThat(order.readyAt()).isNotNull();
    }
    @Test
    void givenOrderNotInPaidStatus_whenMarkAsReady_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().build();

        assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::markAsReady);

        assertThat(order.status()).isEqualTo(OrderStatus.DRAFT);
        assertThat(order.readyAt()).isNull();
    }
}

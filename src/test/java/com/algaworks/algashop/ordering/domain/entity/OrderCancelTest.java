package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class OrderCancelTest {

    @Test
    void givenOrderInCancellableStatus_whenCancel_shouldChangeStatusToCanceled() {
        Order order = OrderTestDataBuilder.anOrder().build();

        order.cancel();

        assertThat(order.status()).isEqualTo(OrderStatus.CANCELED);
        assertThat(order.canceledAt()).isNotNull();
    }

    @Test
    void givenAlreadyCanceledOrder_whenCancel_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().build();
        order.cancel();
        assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::cancel);
    }
}

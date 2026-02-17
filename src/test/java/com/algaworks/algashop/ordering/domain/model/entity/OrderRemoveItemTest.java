package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exception.OrderCannotBeEditedException;
import com.algaworks.algashop.ordering.domain.model.exception.OrderDoesNotContainOrderItemException;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.OrderItemId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;


import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class OrderRemoveItemTest {

    @Test
    void givenDraftOrderWithTwoItems_whenRemoveOneItem_shouldSucceedAndRecalculateTotals() {
        Order order = Order.draft(new CustomerId());

        order.addItem(
                ProductTestDataBuilder.aProduct().build(),
                new Quantity(2)
        );
        OrderItem orderItem1 = order.items().iterator().next();
        order.addItem(
                ProductTestDataBuilder.aProductAltRamMemory().build(),
                new Quantity(3)
        );
        order.removeItem(orderItem1.id());

        Assertions.assertWith(order,
                (i) -> Assertions.assertThat(i.totalAmount()).isEqualTo(new Money("600.00")),
                (i) -> Assertions.assertThat(i.totalItems()).isEqualTo(new Quantity(3))
        );
    }

    @Test
    void givenDraftOrder_whenRemoveNonExistentItem_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderItemId nonExistentItemId = new OrderItemId();

        assertThatExceptionOfType(OrderDoesNotContainOrderItemException.class)
                .isThrownBy(() -> order.removeItem(nonExistentItemId));
    }

    @Test
    void givenPlacedOrder_whenRemoveItem_shouldThrowException() {
        Order order = OrderTestDataBuilder.anOrder().build();
        order.place();
        OrderItemId itemToRemove = order.items().stream().findFirst().orElseThrow().id();

        assertThatExceptionOfType(OrderCannotBeEditedException.class)
                .isThrownBy(() -> order.removeItem(itemToRemove));
    }
}

package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exception.ProductOutOfStockException;
import com.algaworks.algashop.ordering.domain.model.exception.ShoppingCartDoesNotContainItemException;
import com.algaworks.algashop.ordering.domain.model.exception.ShoppingCartDoesNotContainProductException;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ShoppingCartTest {

    @Test
    void givenNewShoppingCart_whenStartShopping_shouldBeEmptyWithZeroTotals() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());

        assertThat(cart.totalAmount()).isEqualTo(Money.ZERO);
        assertThat(cart.totalItems()).isEqualTo(Quantity.ZERO);
        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.items()).isEmpty();
    }

    @Test
    void givenProductOutOfStock_whenAddItem_shouldThrowException() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());
        Product product = ProductTestDataBuilder.aProductUnavailable().build();

        assertThatExceptionOfType(ProductOutOfStockException.class)
                .isThrownBy(() -> cart.addItem(product, new Quantity(1)));
    }

    @Test
    void givenSameProductAddedTwice_whenAddItem_shouldSumQuantityAndRecalculateTotals() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();

        cart.addItem(product, new Quantity(1));
        cart.addItem(product, new Quantity(2));

        assertThat(cart.items()).hasSize(1);
        assertThat(cart.totalItems()).isEqualTo(new Quantity(3));
        assertThat(cart.totalAmount()).isEqualTo(new Money("300"));
    }

    @Test
    void givenTwoDifferentProducts_whenAddItem_shouldAddTwoDistinctItemsAndRecalculate() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());
        Product mousepad = ProductTestDataBuilder.aProductAltMousePad().build();
        Product ram = ProductTestDataBuilder.aProductAltRamMemory().build();

        cart.addItem(mousepad, new Quantity(1));
        cart.addItem(ram, new Quantity(2));

        assertThat(cart.items()).hasSize(2);
        assertThat(cart.totalItems()).isEqualTo(new Quantity(3));
        assertThat(cart.totalAmount()).isEqualTo(new Money("500"));
    }

    @Test
    void givenCartWithItem_whenRemoveNonExistentItem_shouldThrowException() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());
        cart.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(1));
        ShoppingCartItem nonExistentItem = ShoppingCartItemTestDataBuilder.anItem().build();

        assertThatExceptionOfType(ShoppingCartDoesNotContainItemException.class)
                .isThrownBy(() -> cart.removeItem(nonExistentItem.id()));
    }

    @Test
    void givenCartWithItems_whenEmpty_shouldRemoveAllItemsAndZeroTotals() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());
        cart.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(1));
        cart.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(), new Quantity(1));

        cart.empty();

        assertThat(cart.isEmpty()).isTrue();
        assertThat(cart.totalAmount()).isEqualTo(Money.ZERO);
        assertThat(cart.totalItems()).isEqualTo(Quantity.ZERO);
    }

    @Test
    void givenCartWithItem_whenRefreshWithIncompatibleProduct_shouldThrowException() {
        ShoppingCart cart = ShoppingCart.startShopping(new CustomerId());
        cart.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(1));
        Product incompatibleProduct = ProductTestDataBuilder.aProductAltRamMemory().build();

        assertThatExceptionOfType(ShoppingCartDoesNotContainProductException.class)
                .isThrownBy(() -> cart.refreshItem(incompatibleProduct));
    }

    @Test
    void givenTwoCartsWithSameId_whenCheckEquality_shouldBeEqual() {
        ShoppingCart cart1 = ShoppingCart.startShopping(new CustomerId());
        ShoppingCart cart2 = ShoppingCart.builder()
                .id(cart1.id())
                .customerId(new CustomerId())
                .totalAmount(Money.ZERO)
                .totalItems(Quantity.ZERO)
                .createdAt(OffsetDateTime.now())
                .items(new HashSet<>())
                .build();

        assertThat(cart1).isEqualTo(cart2);
        assertThat(cart1.hashCode()).isEqualTo(cart2.hashCode());
    }
}

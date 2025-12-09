package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.ShoppingCartItemIncompatibleProductException;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ShoppingCartItemTest {

    @Test
    void givenNewItem_whenBuildWithBrandNew_shouldCalculateTotalAmount() {
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();
        Quantity quantity = new Quantity(3);


        ShoppingCartItem item = ShoppingCartItem.brandNew()
                .shoppingCartId(new ShoppingCartId())
                .product(product)
                .quantity(quantity)
                .build();

        assertThat(item.totalAmount()).isEqualTo(new Money("300"));
    }

    @Test
    void givenItem_whenChangeQuantityToZero_shouldThrowException() {
        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.anItem().build();

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> item.changeQuantity(Quantity.ZERO));
    }

    @Test
    void givenItem_whenRefreshWithIncompatibleProduct_shouldThrowException() {
        ShoppingCartItem item = ShoppingCartItemTestDataBuilder.anItem().build();
        Product incompatibleProduct = ProductTestDataBuilder.aProductAltRamMemory().build();

        assertThatExceptionOfType(ShoppingCartItemIncompatibleProductException.class)
                .isThrownBy(() -> item.refresh(incompatibleProduct));
    }

    @Test
    void givenTwoItemsWithSameId_whenCheckEquality_shouldBeEqual() {
        ShoppingCartItem item1 = ShoppingCartItemTestDataBuilder.anItem().build();
        ShoppingCartItem item2 = ShoppingCartItem.existing()
                .id(item1.id())
                .shoppingCartId(item1.shoppingCartId())
                .productId(item1.productId())
                .productName(item1.productName())
                .price(item1.price())
                .quantity(new Quantity(99))
                .totalAmount(new Money("9999"))
                .available(item1.isAvailable())
                .build();

        assertThat(item1).isEqualTo(item2);
        assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
    }
}

package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.Product;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;

public class ShoppingCartItemTestDataBuilder {

    private ShoppingCartItemTestDataBuilder() {
    }

    public static ShoppingCartItem.ExistingBuilder anItem() {
        Product product = ProductTestDataBuilder.aProductAltMousePad().build();
        Quantity quantity = new Quantity(2);
        Money totalAmount = product.price().multiply(quantity);

        return ShoppingCartItem.existing()
                .id(new ShoppingCartItemId())
                .shoppingCartId(new ShoppingCartId())
                .productId(product.id())
                .productName(product.name())
                .price(product.price())
                .quantity(quantity)
                .totalAmount(totalAmount)
                .available(product.inStock());
    }
}

package com.algaworks.algashop.ordering.domain.exception;

import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;

import static com.algaworks.algashop.ordering.domain.exception.ErrorMessages.ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_ITEM;

public class ShoppingCartDoesNotContainItemException extends DomainException {
    public ShoppingCartDoesNotContainItemException(ShoppingCartId cartId, ShoppingCartItemId itemId) {
        super(String.format(ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_ITEM, cartId.value(), itemId.value()));
    }

    public ShoppingCartDoesNotContainItemException(ShoppingCartId cartId) {
        super(String.format("Shopping cart %s does not contain the specified item.", cartId.value()));
    }
}

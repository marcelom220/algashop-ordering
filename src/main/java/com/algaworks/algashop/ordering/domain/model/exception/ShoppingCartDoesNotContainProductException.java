package com.algaworks.algashop.ordering.domain.model.exception;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;

import static com.algaworks.algashop.ordering.domain.model.exception.ErrorMessages.ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT;

public class ShoppingCartDoesNotContainProductException extends DomainException {
    public ShoppingCartDoesNotContainProductException(ShoppingCartId cartId, ProductId productId) {
        super(String.format(ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_PRODUCT, cartId.value(), productId.value()));
    }

    public ShoppingCartDoesNotContainProductException(ShoppingCartId cartId) {
        super(String.format("Shopping cart %s does not contain the specified product.", cartId.value()));
    }
}
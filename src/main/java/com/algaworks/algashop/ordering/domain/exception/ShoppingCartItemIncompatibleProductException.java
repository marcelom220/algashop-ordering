package com.algaworks.algashop.ordering.domain.exception;

import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;

import static com.algaworks.algashop.ordering.domain.exception.ErrorMessages.ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT;

public class ShoppingCartItemIncompatibleProductException extends DomainException {
    public ShoppingCartItemIncompatibleProductException(ProductId productId, ShoppingCartItemId itemId) {
        super(String.format(ERROR_SHOPPING_CART_ITEM_INCOMPATIBLE_PRODUCT, productId.value(), itemId.value()));
    }
}

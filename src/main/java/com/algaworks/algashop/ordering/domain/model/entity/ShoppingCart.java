package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exception.ShoppingCartDoesNotContainItemException;
import com.algaworks.algashop.ordering.domain.model.exception.ShoppingCartDoesNotContainProductException;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ShoppingCart implements AggregateRoot<ShoppingCartId>{

    private ShoppingCartId id;
    private CustomerId customerId;
    private Money totalAmount;
    private Quantity totalItems;
    private OffsetDateTime createdAt;
    private Set<ShoppingCartItem> items;

    @Builder
    public ShoppingCart(ShoppingCartId id, CustomerId customerId, Money totalAmount,
                        Quantity totalItems, OffsetDateTime createdAt, Set<ShoppingCartItem> items) {
        this.setId(id);
        this.setCustomerId(customerId);
        this.setTotalAmount(totalAmount);
        this.setTotalItems(totalItems);
        this.setCreatedAt(createdAt);
        this.setItems(items);
    }

    public static ShoppingCart startShopping(CustomerId customerId) {
        return new ShoppingCart(
                new ShoppingCartId(),
                customerId,
                Money.ZERO,
                Quantity.ZERO,
                OffsetDateTime.now(),
                new HashSet<>()
        );
    }

    public void empty() {
        this.items.clear();
        this.recalculateTotals();
    }

    public void addItem(Product product, Quantity quantity) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(quantity);
        product.checkOutOfStock();

        this.items.stream()
                .filter(item -> item.productId().equals(product.id()))
                .findFirst()
                .ifPresentOrElse(
                        item -> {
                            item.changeQuantity(item.quantity().add(quantity));
                            item.refresh(product);
                        },
                        () -> {
                            ShoppingCartItem newItem = ShoppingCartItem.brandNew()
                                    .shoppingCartId(this.id)
                                    .product(product)
                                    .quantity(quantity)
                                    .build();
                            this.items.add(newItem);
                        }
                );
        this.recalculateTotals();
    }

    public void removeItem(ShoppingCartItemId itemId) {
        ShoppingCartItem item = findItem(itemId);
        this.items.remove(item);
        this.recalculateTotals();
    }

    public void refreshItem(Product product) {
        Objects.requireNonNull(product);
        ShoppingCartItem item = findItem(product.id());
        item.refresh(product);
        this.recalculateTotals();
    }

    public void changeItemQuantity(ShoppingCartItemId itemId, Quantity quantity) {
        ShoppingCartItem item = findItem(itemId);
        item.changeQuantity(quantity);
        this.recalculateTotals();
    }

    public ShoppingCartItem findItem(ShoppingCartItemId itemId) {
        Objects.requireNonNull(itemId);
        return this.items.stream()
                .filter(item -> item.id().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartDoesNotContainItemException(this.id, itemId));
    }

    public ShoppingCartItem findItem(ProductId productId) {
        Objects.requireNonNull(productId);
        return this.items.stream()
                .filter(item -> item.productId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ShoppingCartDoesNotContainProductException(this.id, productId));
    }

    private void recalculateTotals() {
        BigDecimal totalAmountValue = this.items.stream()
                .map(item -> item.totalAmount().value())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        this.setTotalAmount(new Money(totalAmountValue));

        Integer totalItemsValue = this.items.stream()
                .map(item -> item.quantity().value())
                .reduce(0, Integer::sum);
        this.setTotalItems(new Quantity(totalItemsValue));
    }

    public boolean containsUnavailableItems() {
        return this.items.stream().anyMatch(item -> !item.isAvailable());
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    public ShoppingCartId id() { return id; }
    public CustomerId customerId() { return customerId; }
    public Money totalAmount() { return totalAmount; }
    public Quantity totalItems() { return totalItems; }
    public OffsetDateTime createdAt() { return createdAt; }
    public Set<ShoppingCartItem> items() { return Collections.unmodifiableSet(items); }

    private void setId(ShoppingCartId id) { this.id = Objects.requireNonNull(id); }
    private void setCustomerId(CustomerId customerId) { this.customerId = Objects.requireNonNull(customerId); }
    private void setTotalAmount(Money totalAmount) { this.totalAmount = Objects.requireNonNull(totalAmount); }
    private void setTotalItems(Quantity totalItems) { this.totalItems = Objects.requireNonNull(totalItems); }
    private void setCreatedAt(OffsetDateTime createdAt) { this.createdAt = Objects.requireNonNull(createdAt); }
    private void setItems(Set<ShoppingCartItem> items) { this.items = Objects.requireNonNull(items); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCart that = (ShoppingCart) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

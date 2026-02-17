package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exception.ShoppingCartItemIncompatibleProductException;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import lombok.Builder;

import java.util.Objects;

public class ShoppingCartItem {

    private ShoppingCartItemId id;
    private ShoppingCartId shoppingCartId;
    private ProductId productId;
    private ProductName productName;
    private Money price;
    private Quantity quantity;
    private Money totalAmount;
    private Boolean available;

    @Builder(builderClassName = "BrandNewBuilder", builderMethodName = "brandNew")
    public ShoppingCartItem(ShoppingCartId shoppingCartId, Product product, Quantity quantity) {
        this.setId(new ShoppingCartItemId());
        this.setShoppingCartId(shoppingCartId);
        this.setProductId(product.id());
        this.setProductName(product.name());
        this.setPrice(product.price());
        this.setQuantity(quantity);
        this.setAvailable(product.inStock());
        this.recalculateTotals();
    }

    @Builder(builderClassName = "ExistingBuilder", builderMethodName = "existing")
    public ShoppingCartItem(ShoppingCartItemId id, ShoppingCartId shoppingCartId, ProductId productId,
                            ProductName productName, Money price, Quantity quantity, Money totalAmount, Boolean available) {
        this.setId(id);
        this.setShoppingCartId(shoppingCartId);
        this.setProductId(productId);
        this.setProductName(productName);
        this.setPrice(price);
        this.setQuantity(quantity);
        this.setTotalAmount(totalAmount);
        this.setAvailable(available);
    }

    public void refresh(Product product) {
        Objects.requireNonNull(product);
        if (!this.productId.equals(product.id())) {
            throw new ShoppingCartItemIncompatibleProductException(product.id(), this.id);
        }
        this.setPrice(product.price());
        this.setProductName(product.name());
        this.setAvailable(product.inStock());
        this.recalculateTotals();
    }

    public void changeQuantity(Quantity quantity) {
        Objects.requireNonNull(quantity);
        if (quantity.value() <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero.");
        }
        this.setQuantity(quantity);
        this.recalculateTotals();
    }

    private void recalculateTotals() {
        this.setTotalAmount(this.price.multiply(this.quantity));
    }

    public ShoppingCartItemId id() { return id; }
    public ShoppingCartId shoppingCartId() { return shoppingCartId; }
    public ProductId productId() { return productId; }
    public ProductName productName() { return productName; }
    public Money price() { return price; }
    public Quantity quantity() { return quantity; }
    public Money totalAmount() { return totalAmount; }
    public Boolean isAvailable() { return available; }

    private void setId(ShoppingCartItemId id) { this.id = Objects.requireNonNull(id); }
    private void setShoppingCartId(ShoppingCartId shoppingCartId) { this.shoppingCartId = Objects.requireNonNull(shoppingCartId); }
    private void setProductId(ProductId productId) { this.productId = Objects.requireNonNull(productId); }
    private void setProductName(ProductName productName) { this.productName = Objects.requireNonNull(productName); }
    private void setPrice(Money price) { this.price = Objects.requireNonNull(price); }
    private void setQuantity(Quantity quantity) { this.quantity = Objects.requireNonNull(quantity); }
    private void setTotalAmount(Money totalAmount) { this.totalAmount = Objects.requireNonNull(totalAmount); }
    private void setAvailable(Boolean available) { this.available = Objects.requireNonNull(available); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartItem that = (ShoppingCartItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}

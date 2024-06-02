package com.example.demo.cartItems.api;

import java.util.Objects;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class CartItemDto {
    private Long id;

    @NotNull
    @Min(1)
    private Long productId;

    @NotNull
    @Min(1)
    private Integer quantity;

    private String productName;
    private Double productPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String name) {
        this.productName = name;
    }

    public Double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Double price) {
        this.productPrice = price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        CartItemDto other = (CartItemDto) obj;
        return Objects.equals(productId, other.productId) && Objects.equals(quantity, other.quantity);
    }
}
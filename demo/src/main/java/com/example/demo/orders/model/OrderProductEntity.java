package com.example.demo.orders.model;

import java.util.Objects;

import com.example.demo.core.model.BaseEntity;
import com.example.demo.product.model.ProductEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "orderProducts")
public class OrderProductEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "productId", nullable = false)
    private ProductEntity product;
    @ManyToOne
    @JoinColumn(name = "orderId", nullable = false)
    private OrderEntity order;
    @Column(nullable = false)
    private Integer quantity;

    public OrderProductEntity() {
    }

    public OrderProductEntity(ProductEntity product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public OrderEntity getOrder() {
        return order;
    }

    public void setOrder(OrderEntity order) {
        this.order = order;
        if (!order.getItems().contains(this)) {
            order.getItems().add(this);
        }
    }

    public ProductEntity getProduct() {
        return product;
    }

    public void setProduct(ProductEntity product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, order, product, quantity);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final OrderProductEntity other = (OrderProductEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getOrder(), order)
                && Objects.equals(other.getProduct(), product)
                && Objects.equals(other.getQuantity(), quantity);
    }
}

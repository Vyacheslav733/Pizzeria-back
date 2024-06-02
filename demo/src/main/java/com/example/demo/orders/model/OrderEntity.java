package com.example.demo.orders.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.example.demo.core.model.BaseEntity;
import com.example.demo.users.model.UserEntity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class OrderEntity extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private Date date = new Date();

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @OrderBy("id ASC")
    private List<OrderProductEntity> items = new ArrayList<>();

    public OrderEntity() {
    }

    public OrderEntity(UserEntity user, Date date) {
        this.user = user;
        this.date = date;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
        if (!user.getOrders().contains(this)) {
            user.getOrders().add(this);
        }
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<OrderProductEntity> getItems() {
        return items;
    }

    public void setItems(List<OrderProductEntity> items) {
        this.items = items;
    }

    public void addItem(OrderProductEntity orderProduct) {
        if (orderProduct.getOrder() != this) {
            orderProduct.setOrder(this);
        }

        items.add(orderProduct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, items, date);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final OrderEntity other = (OrderEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getItems(), items)
                && Objects.equals(other.getDate(), date)
                && Objects.equals(other.getUser(), user);
    }
}

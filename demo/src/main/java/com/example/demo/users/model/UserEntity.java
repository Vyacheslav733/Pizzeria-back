package com.example.demo.users.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

import com.example.demo.core.model.BaseEntity;
import com.example.demo.orders.model.OrderEntity;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {
    @Column(nullable = false, unique = true, length = 20)
    private String login;
    @Column(nullable = false)
    private String password;

    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @OrderBy("id ASC")
    private Set<OrderEntity> orders = new HashSet<>();

    public UserEntity() {
    }

    public UserEntity(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String name) {
        this.login = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<OrderEntity> getOrders() {
        return orders;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public void addOrder(OrderEntity order) {
        if (order.getUser() != this) {
            order.setUser(this);
        }
        orders.add(order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password, orders, role);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final UserEntity other = (UserEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getLogin(), login)
                && Objects.equals(other.getPassword(), password)
                && Objects.equals(other.getRole(), role)
                && Objects.equals(other.getOrders(), orders);
    }
}

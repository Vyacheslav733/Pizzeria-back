package com.example.demo.product.model;

import java.util.Objects;

import com.example.demo.types.model.TypeEntity;
import com.example.demo.core.model.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class ProductEntity extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "typeId", nullable = false)
    private TypeEntity type;
    @Column(nullable = false)
    private Double price;

    public ProductEntity() {
    }

    public ProductEntity(String name, TypeEntity type, Double price) {
        this.name = name;
        this.type = type;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TypeEntity getType() {
        return type;
    }

    public void setType(TypeEntity type) {
        this.type = type;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, price/* , orderProducts*/);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        final ProductEntity other = (ProductEntity) obj;
        return Objects.equals(other.getId(), id)
                && Objects.equals(other.getName(), name)
                && Objects.equals(other.getType(), type)
                && Objects.equals(other.getPrice(), price);
    }
}

package com.example.demo.orders.api;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class OrderDto {
    private Long id;

    private Date date;

    @NotNull
    @Size(min = 1)
    private List<OrderProductDto> orderProducts;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderProductDto> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProductDto> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
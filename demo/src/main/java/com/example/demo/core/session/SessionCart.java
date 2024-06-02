package com.example.demo.core.session;

import java.util.HashMap;

import com.example.demo.cartItems.api.CartItemDto;

public class SessionCart extends HashMap<Integer, CartItemDto> {
    public double getSum() {
        return this.values().stream()
                .map(item -> item.getQuantity() * item.getProductPrice())
                .mapToDouble(Double::doubleValue)
                .sum();
    }
}

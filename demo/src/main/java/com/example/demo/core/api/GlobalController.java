package com.example.demo.core.api;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.demo.core.session.SessionCart;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalController {
    private final SessionCart cart;

    public GlobalController(SessionCart cart) {
        this.cart = cart;
    }

    @ModelAttribute("servletPath")
    String getRequestServletPath(HttpServletRequest request) {
        return request.getServletPath();
    }

    @ModelAttribute("totalCart")
    double getTotalCart() {
        return cart.getSum();
    }
}

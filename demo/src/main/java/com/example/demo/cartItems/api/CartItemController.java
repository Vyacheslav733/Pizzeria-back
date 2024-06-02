package com.example.demo.cartItems.api;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.example.demo.core.configuration.Constants;
import com.example.demo.core.security.UserPrincipal;
import com.example.demo.core.session.SessionCart;
import com.example.demo.orders.api.OrderProductDto;
import com.example.demo.orders.service.OrderService;
import com.example.demo.product.api.ProductDto;
import com.example.demo.product.model.ProductEntity;
import com.example.demo.product.service.ProductService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(CartItemController.URL)
@SessionAttributes("products")
public class CartItemController {
    public static final String URL = "/cart";
    private static final String ORDER_VIEW = "cart";
    private static final String CART_ITEM_ATTRIBUTE = "cartItem";
    private static final String CART_ATTRIBUTE = "cart";

    private final OrderService orderService;
    private final ProductService productService;
    private final ModelMapper modelMapper;
    private final SessionCart cart;

    public CartItemController(ProductService productService, SessionCart cart, OrderService orderService,
            ModelMapper modelMapper) {
        this.productService = productService;
        this.orderService = orderService;
        this.modelMapper = modelMapper;
        this.cart = cart;
    }

    private ProductDto toProductDto(ProductEntity entity) {
        return modelMapper.map(entity, ProductDto.class);
    }

    private List<OrderProductDto> toOrderProductDtos(Collection<CartItemDto> cartItems) {
        return cartItems.stream().map(item -> {
            final OrderProductDto dto = new OrderProductDto();
            dto.setQuantity(item.getQuantity());
            dto.setProductId(item.getProductId());
            return dto;
        }).toList();
    }

    @GetMapping
    public String getCart(Model model, @AuthenticationPrincipal UserPrincipal principal) {
        model.addAttribute("products",
                productService.getAll(0L).stream()
                        .map(this::toProductDto)
                        .toList());
        model.addAttribute(CART_ITEM_ATTRIBUTE, new CartItemDto());
        model.addAttribute(CART_ATTRIBUTE, cart.values());
        return ORDER_VIEW;
    }

    @PostMapping
    public String addItemToCart(
            @ModelAttribute(name = CART_ITEM_ATTRIBUTE) @Valid CartItemDto cartItem,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserPrincipal principal,
            SessionStatus status,
            Model model) {
        if (bindingResult.hasErrors()) {
            return ORDER_VIEW;
        }

        status.setComplete();

        ProductEntity product = productService.get(cartItem.getProductId());
        cartItem.setProductName(product.getName());
        cartItem.setProductPrice(product.getPrice());

        cart.computeIfPresent(cartItem.hashCode(), (key, value) -> {
            value.setQuantity(value.getQuantity() + cartItem.getQuantity());
            return value;
        });
        cart.putIfAbsent(cartItem.hashCode(), cartItem);
        return Constants.REDIRECT_VIEW + URL;
    }

    @PostMapping("/save")
    public String createOrder(
            Model model,
            @AuthenticationPrincipal UserPrincipal principal) {
        orderService.create(principal.getId(), toOrderProductDtos(cart.values()));
        cart.clear();
        return Constants.REDIRECT_VIEW + "/";
    }

    @PostMapping("/clear")
    public String clearCart(@AuthenticationPrincipal UserPrincipal principal) {
        cart.clear();
        return Constants.REDIRECT_VIEW + URL;
    }

    @PostMapping("/toggle-quantity")
    public String increaseCartCount(
            @RequestParam(name = "productId", defaultValue = "0") Long productId,
            @RequestParam(name = "isIncrease", defaultValue = "false") boolean isIncrease,
            @AuthenticationPrincipal UserPrincipal principal) {
        if (productId <= 0) {
            throw new IllegalArgumentException();
        }
        cart.computeIfPresent(Objects.hash(productId), (key, value) -> {
            if (isIncrease) value.setQuantity(value.getQuantity() + 1);
            else if (!isIncrease && value.getQuantity() - 1 > 0) value.setQuantity(value.getQuantity() - 1);
            return value;
        });

        return Constants.REDIRECT_VIEW + URL;
    }
}

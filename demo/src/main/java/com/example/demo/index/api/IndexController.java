package com.example.demo.index.api;

import java.util.Map;
import java.util.Objects;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.support.SessionStatus;

import com.example.demo.cartItems.api.CartItemDto;
import com.example.demo.core.api.PageAttributesMapper;
import com.example.demo.core.configuration.Constants;
import com.example.demo.core.security.UserPrincipal;
import com.example.demo.core.session.SessionCart;
import com.example.demo.product.api.ExpandedProductDto;
import com.example.demo.product.model.ProductEntity;
import com.example.demo.product.service.ProductService;

@Controller
public class IndexController {
    public static final String PRODUCTS_VIEW = "index";
    private static final String PAGE_ATTRIBUTE = "page";

    private final ProductService productService;
    private final SessionCart cart;
    private final ModelMapper modelMapper;

    public IndexController(ProductService productService,
            ModelMapper modelMapper, SessionCart cart) {
        this.productService = productService;
        this.cart = cart;
        this.modelMapper = modelMapper;
    }

    protected ExpandedProductDto toExpandedDto(ProductEntity entity, Long userId) {
        ExpandedProductDto dto = modelMapper.map(entity, ExpandedProductDto.class);
        dto.setTypeName(entity.getType().getName());
        final var t = cart.containsKey(Objects.hash(dto.getId()));
        dto.setIsCartProduct(cart.containsKey(Objects.hash(dto.getId())));
        return dto;
    }

    @GetMapping
    public String getAll(
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal) {
        final Map<String, Object> attributes = PageAttributesMapper.toAttributes(
                productService.getAll(0L, page, Constants.DEFAULT_PAGE_SIZE),
                item -> toExpandedDto(item, principal.getId()));
        model.addAllAttributes(attributes);
        model.addAttribute(PAGE_ATTRIBUTE, page);

        return PRODUCTS_VIEW;
    }

    @PostMapping("/buy")
    public String addProductToCart(
            Model model,
            @RequestParam(name = "id") Long productId,
            @RequestParam(name = "isCartProduct") boolean isCartProduct,
            SessionStatus status,
            @AuthenticationPrincipal UserPrincipal principal) {
        if (productId <= 0) {
            throw new IllegalArgumentException();
        }

        status.setComplete();
        if (!isCartProduct)
            buyProduct(productId);
        else
            increaseCartItemQuantity(productId);

        return Constants.REDIRECT_VIEW + "/";
    }

    private void buyProduct(Long productId) {
        ProductEntity product = productService.get(productId);
        CartItemDto cartItem = new CartItemDto();
        cartItem.setProductId(productId);
        cartItem.setQuantity(1);
        cartItem.setProductName(product.getName());
        cartItem.setProductPrice(product.getPrice());

        cart.putIfAbsent(cartItem.hashCode(), cartItem);
    }

    private void increaseCartItemQuantity(Long productId) {
        cart.computeIfPresent(Objects.hash(productId), (key, value) -> {
            value.setQuantity(value.getQuantity() + 1);
            return value;
        });
    }
}

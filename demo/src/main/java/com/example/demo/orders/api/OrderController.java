package com.example.demo.orders.api;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.core.api.PageAttributesMapper;
import com.example.demo.core.configuration.Constants;
import com.example.demo.core.security.UserPrincipal;
import com.example.demo.orders.model.OrderEntity;
import com.example.demo.orders.model.OrderProductEntity;
import com.example.demo.orders.service.OrderProductService;
import com.example.demo.orders.service.OrderService;
import com.example.demo.product.model.ProductEntity;
import com.example.demo.product.service.ProductService;

@Controller
@RequestMapping(OrderController.URL)
public class OrderController {
    public static final String URL = "/orders";
    private static final String ORDER_VIEW = "orders";
    private static final String ORDER_DETAILS_VIEW = "order-details";

    private static final String ORDER_ATTRIBUTE = "order";
    private static final String PAGE_ATTRIBUTE = "page";

    private final OrderService orderService;
    private final OrderProductService orderProductService;
    private final ModelMapper modelMapper;
    private final ProductService productService;

    public OrderController(
            OrderService orderService,
            OrderProductService orderProductService,
            ProductService productService,
            ModelMapper modelMapper) {
        this.orderService = orderService;
        this.productService = productService;
        this.orderProductService = orderProductService;
        this.modelMapper = modelMapper;
    }

    private OrderDto toDto(OrderEntity entity) {
        return modelMapper.map(entity, OrderDto.class);
    }

    private ExpandedOrderProductDto toOrderProductDto(OrderProductEntity entity) {
        ExpandedOrderProductDto dto = modelMapper.map(entity, ExpandedOrderProductDto.class);
        ProductEntity product = productService.get(dto.getProductId());
        dto.setProductName(product.getName());
        dto.setProductPrice(product.getPrice());
        return dto;
    }

    @GetMapping
    public String getProfile(
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal) {
        final long userId = principal.getId();
        model.addAttribute(PAGE_ATTRIBUTE, page);
        model.addAllAttributes(PageAttributesMapper.toAttributes(
                orderService.getAll(userId, page, Constants.DEFAULT_PAGE_SIZE),
                this::toDto));
        return ORDER_VIEW;
    }

    @GetMapping("/details/{id}")
    public String getOrderDetails(
            @PathVariable(name = "id") Long id,
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal) {
        final long userId = principal.getId();
        model.addAttribute(ORDER_ATTRIBUTE, orderService.get(userId, id));
        model.addAllAttributes(PageAttributesMapper.toAttributes(
                orderProductService.getAll(id, page, Constants.DEFAULT_PAGE_SIZE),
                this::toOrderProductDto));
        return ORDER_DETAILS_VIEW;
    }
}

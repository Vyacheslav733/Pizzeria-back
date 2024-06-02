package com.example.demo.orders.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.orders.api.OrderProductDto;
import com.example.demo.orders.model.ExpandedOrderEntity;
import com.example.demo.orders.model.OrderEntity;
import com.example.demo.orders.model.OrderProductEntity;
import com.example.demo.orders.repository.OrderRepository;
import com.example.demo.product.service.ProductService;
import com.example.demo.users.service.UserService;

@Service
public class OrderService {
    private final UserService userService;
    private final OrderRepository repository;
    private final OrderProductService orderProductService;
    private final ProductService productService;

    public OrderService(OrderRepository repository,
            OrderProductService orderProductService,
            UserService userService, ProductService productService) {
        this.repository = repository;
        this.orderProductService = orderProductService;
        this.userService = userService;
        this.productService = productService;
    }

    @Transactional(readOnly = true)
    public List<OrderEntity> getAll(Long userId) {
        userService.get(userId);
        return repository.findByUserId(userId);
    }

    @Transactional(readOnly = true)
    public Page<OrderEntity> getAll(long userId, int page, int size) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        userService.get(userId);
        return repository.findByUserId(userId, pageable);
    }

    @Transactional(readOnly = true)
    public ExpandedOrderEntity get(Long userId, Long id) {
        userService.get(userId);
        return repository.findOrderWithTotal(id);
    }

    @Transactional
    public OrderEntity create(Long userId, List<OrderProductDto> list) {
        OrderEntity entity = new OrderEntity(userService.get(userId), new Date());

        List<OrderProductEntity> orderProducts = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            OrderProductEntity oie = new OrderProductEntity(productService.get(list.get(i)
                    .getProductId()),
                    list.get(i).getQuantity());
            oie.setOrder(entity);
            orderProducts.add(oie);
        }

        entity.setItems(orderProducts);

        OrderEntity order = repository.save(entity);
        orderProductService.createMany(order.getItems());

        return order;
    }
}

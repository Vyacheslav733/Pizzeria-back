package com.example.demo.orders.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.core.error.NotFoundException;
import com.example.demo.orders.model.OrderProductEntity;
import com.example.demo.orders.repository.OrderProductRepository;

@Service
public class OrderProductService {
    private final OrderProductRepository repository;

    public OrderProductService(OrderProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<OrderProductEntity> getAll(Long orderId) {
        return repository.findByOrderId(orderId);
    }

    @Transactional(readOnly = true)
    public Page<OrderProductEntity> getAll(long orderId, int page, int size) {
        final Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return repository.findByOrderId(orderId, pageable);
    }

    @Transactional(readOnly = true)
    public OrderProductEntity get(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(OrderProductEntity.class, id));
    }

    @Transactional
    public List<OrderProductEntity> createMany(List<OrderProductEntity> orderProducts) {
        repository.saveAll(orderProducts);
        return orderProducts;
    }
}

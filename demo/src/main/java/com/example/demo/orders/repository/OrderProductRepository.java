package com.example.demo.orders.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo.orders.model.OrderProductEntity;

public interface OrderProductRepository extends CrudRepository<OrderProductEntity, Long>,
        PagingAndSortingRepository<OrderProductEntity, Long> {
    List<OrderProductEntity> findByOrderId(long orderId);

    Page<OrderProductEntity> findByOrderId(long orderId, Pageable pageable);
}

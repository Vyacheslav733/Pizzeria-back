package com.example.demo.orders.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import com.example.demo.orders.model.ExpandedOrderEntity;
import com.example.demo.orders.model.OrderEntity;

public interface OrderRepository extends CrudRepository<OrderEntity, Long>,
        PagingAndSortingRepository<OrderEntity, Long> {
    List<OrderEntity> findByUserId(long userId);

    Page<OrderEntity> findByUserId(long userId, Pageable pageable);

    Optional<OrderEntity> findByUserIdAndId(long userId, long id);

    @Query("SELECT NEW com.example.demo.orders.model.ExpandedOrderEntity(o, SUM(oi.quantity * p.price)) " +
            "FROM OrderEntity o " +
            "JOIN o.items oi " +
            "JOIN oi.product p " +
            "WHERE o.id = :orderId " +
            "GROUP BY o")
    ExpandedOrderEntity findOrderWithTotal(@Param("orderId") Long orderId);
}

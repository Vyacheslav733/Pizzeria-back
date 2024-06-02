package com.example.demo;

import java.util.Arrays;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.core.error.NotFoundException;
import com.example.demo.orders.api.OrderProductDto;
import com.example.demo.orders.model.OrderEntity;
import com.example.demo.orders.service.OrderService;
import com.example.demo.product.model.ProductEntity;
import com.example.demo.product.service.ProductService;
import com.example.demo.types.model.TypeEntity;
import com.example.demo.types.service.TypeService;
import com.example.demo.users.model.UserEntity;
import com.example.demo.users.service.UserService;


@SpringBootTest
public class OrderServiceTests {
	@Autowired
    private TypeService typeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    private OrderEntity order;

    private UserEntity user;

    private ProductEntity product1;
    private ProductEntity product2;
    private ProductEntity product3;

    @BeforeEach
    void createData() {
        removeData();

        final var type = typeService.create(new TypeEntity("Пепперони"));
        product1 = productService.create(new ProductEntity("test1", type, 399.00));
		product2 = productService.create(new ProductEntity("test2", type, 499.00));
		product3 = productService.create(new ProductEntity("test3", type, 450.50));
        user = userService.create(new UserEntity("kruviii", "Павел", "Крылов", "test@test.ru", "11-11-2023", "+79876543210", "qwerty123"));
        order = orderService.create(user.getId(), new OrderEntity("ул. Северный венец, 32"), Arrays.asList(
				new OrderProductDto(product1.getId(), 3),
				new OrderProductDto(product2.getId(), 1),
				new OrderProductDto(product3.getId(), 2)));
        orderService.create(user.getId(), new OrderEntity("ул. Северный венец, 32"), Arrays.asList(
            new OrderProductDto(product1.getId(), 5)));
        orderService.create(user.getId(), new OrderEntity("ул. Северный венец, 32"), Arrays.asList(
            new OrderProductDto(product2.getId(), 1),
            new OrderProductDto(product3.getId(), 1)));
    }

    @AfterEach
    void removeData() {
        userService.getAll().forEach(item -> userService.delete(item.getId()));
        productService.getAll(0L).forEach(item -> productService.delete(item.getId()));
        typeService.getAll().forEach(item -> typeService.delete(item.getId()));
    }

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> orderService.get(user.getId(), 0L));
    }

    @Transactional
    @Test
    void createTest() {
        Assertions.assertEquals(3, orderService.getAll(user.getId()).size());
        Assertions.assertEquals(order, orderService.get(user.getId(), order.getId()));
    }

    @Transactional
    @Test
    void updateTest() {
        final OrderEntity newEntity = orderService.update(user.getId(), order.getId(), new OrderEntity("ул. Северный венец, 32"), Arrays.asList(
            new OrderProductDto(product1.getId(), 5)));
        Assertions.assertEquals(3, orderService.getAll(user.getId()).size());
        Assertions.assertEquals(newEntity, orderService.get(user.getId(), order.getId()));
    }

    @Test
    void deleteTest() {
        orderService.delete(user.getId(), order.getId());
        Assertions.assertEquals(2, orderService.getAll(user.getId()).size());

        final OrderEntity newEntity = orderService.create(user.getId(), new OrderEntity("ул. Северный венец, 32"), Arrays.asList(
            new OrderProductDto(product1.getId(), 5)));
        Assertions.assertEquals(3, orderService.getAll(user.getId()).size());
        Assertions.assertNotEquals(order.getId(), newEntity.getId());
    }

    @Test
    void getSumTest() {
        Double sum = orderService.getSum(user.getId(), order.getId());
        Assertions.assertEquals(product1.getPrice() * 3 + product2.getPrice() + product3.getPrice() * 2, sum);
    }

    @Test
    void getFullCountTest() {
        Integer count = orderService.getFullCount(user.getId(), order.getId());
        Assertions.assertEquals(6, count);
    }
}

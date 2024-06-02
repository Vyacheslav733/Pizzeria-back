package com.example.demo;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.demo.orders.api.OrderProductDto;
import com.example.demo.orders.service.OrderService;
import com.example.demo.product.model.ProductEntity;
import com.example.demo.product.service.ProductService;
import com.example.demo.types.model.TypeEntity;
import com.example.demo.types.service.TypeService;
import com.example.demo.users.model.UserEntity;
import com.example.demo.users.model.UserRole;
import com.example.demo.users.service.UserService;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {
	private final Logger log = LoggerFactory.getLogger(DemoApplication.class);

	private final TypeService typeService;
	private final ProductService productService;
	private final OrderService orderService;
	private final UserService userService;

	public DemoApplication(TypeService typeService, ProductService productService, OrderService orderService,
			UserService userService) {
		this.typeService = typeService;
		this.productService = productService;
		this.orderService = orderService;
		this.userService = userService;
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// if (args.length > 0 && Arrays.asList(args).contains("--populate")) {
		log.info("Create default types values");
		final var type1 = typeService.create(new TypeEntity("Мясная"));
		final var type2 = typeService.create(new TypeEntity("Пепперони"));
		final var type3 = typeService.create(new TypeEntity("Сырная"));

		log.info("Create default product values");
		final var product1 = productService.create(new ProductEntity("test1", type1, 399.00));
		final var product2 = productService.create(new ProductEntity("test2", type1, 499.00));
		productService.create(new ProductEntity("test3", type2, 450.50));
		productService.create(new ProductEntity("test4", type2, 900.50));
		productService.create(new ProductEntity("test5", type2, 600.00));
		productService.create(new ProductEntity("test6", type3, 750.00));

		log.info("Create default user values");
		final var user1 = userService.create(new UserEntity("user", "user"));

		final var admin = new UserEntity("admin", "admin");
		admin.setRole(UserRole.ADMIN);
		userService.create(admin);

		log.info("Create default orders values");

		OrderProductDto product10 = new OrderProductDto();
		product10.setProductId(product1.getId());
		product10.setQuantity(4);

		OrderProductDto product20 = new OrderProductDto();
		product20.setProductId(product2.getId());
		product20.setQuantity(6);

		orderService.create(user1.getId(), Arrays.asList(product10, product20));
	}
}

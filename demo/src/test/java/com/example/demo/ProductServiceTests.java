package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.example.demo.core.error.NotFoundException;
import com.example.demo.product.model.ProductEntity;
import com.example.demo.product.service.ProductService;
import com.example.demo.types.model.TypeEntity;
import com.example.demo.types.service.TypeService;

import jakarta.transaction.Transactional;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class ProductsServiceTests {
    @Autowired
    private TypeService typeService;

    private TypeEntity type;

    @Autowired
    private ProductService productService;

    private ProductEntity product;

    void createData() {
        removeData();

        type = typeService.create(new TypeEntity("Мясная"));
		final var type2 = typeService.create(new TypeEntity("Пепперони"));
		final var type3 = typeService.create(new TypeEntity("Сырная"));


        product = productService.create(new ProductEntity("test", type, 399.00));
		productService.create(new ProductEntity("test", type2, 499.00));
		productService.create(new ProductEntity("test", type3, 450.50));
    }

    void removeData() {
        productService.getAll(0L).forEach(item -> productService.delete(item.getId()));
        typeService.getAll().forEach(item -> typeService.delete(item.getId()));
    }

    @Test
    void getTest() {
        Assertions.assertThrows(NotFoundException.class, () -> productService.get(0L));
    }

    @Transactional
    @Test
    void createTest() {
        Assertions.assertEquals(6, productService.getAll(0L).size());
        product = productService.create(new ProductEntity("test1", type, 399.00));
        Assertions.assertEquals(product, productService.get(product.getId()));
    }

    @Test
    void createNullableTest() {
        final ProductEntity nullableProduct = new ProductEntity(null, type, 399.00);;
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> productService.create(nullableProduct));
    }

    @Transactional
    @Test
    void updateTest() {
        final String test = "TEST";
        final TypeEntity newType = typeService.create(new TypeEntity("Фреш"));
        product = productService.create(new ProductEntity("te2st", type, 399.00));
        final TypeEntity oldType = product.getType();
        final ProductEntity newEntity = productService.update(product.getId(), new ProductEntity(test, newType, 100.00));
        
        Assertions.assertEquals(newEntity, productService.get(product.getId()));
        Assertions.assertEquals(test, newEntity.getName());
        Assertions.assertEquals(newType, newEntity.getType());
        Assertions.assertNotEquals(oldType, newEntity.getType());
    }

    @Test
    void deleteTest() {
        type = typeService.create(new TypeEntity("yynrrnn"));
        product = productService.create(new ProductEntity("te2st", type, 399.00));
        productService.delete(product.getId());
        Assertions.assertEquals(6, productService.getAll(0L).size());

        final ProductEntity newEntity = productService.create(new ProductEntity(product.getName(), product.getType(), product.getPrice()));
        Assertions.assertEquals(7, typeService.getAll().size());
        Assertions.assertNotEquals(product.getId(), newEntity.getId());
    }
}
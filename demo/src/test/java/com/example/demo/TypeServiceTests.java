package com.example.demo;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import com.example.demo.core.error.NotFoundException;
import com.example.demo.types.model.TypeEntity;
import com.example.demo.types.service.TypeService;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
class TypeServiceTests {
	@Autowired
	private TypeService typeService;

	private TypeEntity type;

    void createData() {


        type = typeService.create(new TypeEntity("Пепперони"));
        typeService.create(new TypeEntity("Мясная"));
        typeService.create(new TypeEntity("4 сыра"));
    }

    
    void removeData() {
        typeService.getAll().forEach(item -> typeService.delete(item.getId()));
    }

	@Test
	void getTest() {
		Assertions.assertThrows(NotFoundException.class, () -> typeService.get(0L));
	}

	@Test
	void createTest() {
        type = typeService.create(new TypeEntity("thdgndgneth"));
		Assertions.assertEquals(5, typeService.getAll().size());
        Assertions.assertEquals(type, typeService.get(type.getId()));
	}

	@Test
    void createNotUniqueTest() {
        final TypeEntity nonUniqueType = new TypeEntity("Пепперони");
        Assertions.assertThrows(IllegalArgumentException.class, () -> typeService.create(nonUniqueType));
    }

	@Test
    void createNullableTest() {
        final TypeEntity nullableType = new TypeEntity(null);
        Assertions.assertThrows(DataIntegrityViolationException.class, () -> typeService.create(nullableType));
    }

	@Test
    void updateTest() {
        final String test = "TEST";
        type = typeService.create(new TypeEntity("ryjhjhrrnh"));
        final String oldName = type.getName();
        final TypeEntity newEntity = typeService.update(type.getId(), new TypeEntity(test));
        Assertions.assertEquals(4, typeService.getAll().size());
        Assertions.assertEquals(newEntity, typeService.get(type.getId()));
        Assertions.assertEquals(test, newEntity.getName());
        Assertions.assertNotEquals(oldName, newEntity.getName());
    }

    @Test
    void deleteTest() {
        type = typeService.create(new TypeEntity("gnnetnten"));
        typeService.delete(type.getId());
        Assertions.assertEquals(5, typeService.getAll().size());

        final TypeEntity newEntity = typeService.create(new TypeEntity(type.getName()));
        Assertions.assertEquals(6, typeService.getAll().size());
        Assertions.assertNotEquals(type.getId(), newEntity.getId());
    }
}

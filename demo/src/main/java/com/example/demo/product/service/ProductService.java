package com.example.demo.product.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.StreamSupport;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.core.error.NotFoundException;
import com.example.demo.product.model.ProductEntity;
import com.example.demo.product.repository.ProductRepository;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<ProductEntity> getAll(long typeId) {
        if (typeId <= 0L) {
            return StreamSupport.stream(repository.findAll().spliterator(), false).toList();
        }
        return repository.findByTypeId(typeId);
    }

    @Transactional(readOnly = true)
    public Page<ProductEntity> getAll(Long categoryId, int page, int size) {
        final PageRequest pageable = PageRequest.of(page, size);
        if (Objects.equals(categoryId, 0L)) {
            return repository.findAll(pageable);
        }

        return repository.findByTypeId(categoryId, pageable);
    }

    @Transactional(readOnly = true)
    public ProductEntity get(long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(ProductEntity.class, id));
    }

    @Transactional
    public ProductEntity create(ProductEntity entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity is null");
        }
        return repository.save(entity);
    }

    @Transactional
    public ProductEntity update(Long id, ProductEntity entity) {
        final ProductEntity existsEntity = get(id);
        existsEntity.setName(entity.getName());
        existsEntity.setType(entity.getType());
        existsEntity.setPrice(entity.getPrice());
        return repository.save(existsEntity);
    }

    @Transactional
    public ProductEntity delete(Long id) {
        final ProductEntity existsEntity = get(id);
        repository.delete(existsEntity);
        return existsEntity;
    }
}

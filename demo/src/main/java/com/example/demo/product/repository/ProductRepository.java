package com.example.demo.product.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.example.demo.product.model.ProductEntity;

public interface ProductRepository extends CrudRepository<ProductEntity, Long>, PagingAndSortingRepository<ProductEntity, Long> {
    List<ProductEntity> findByTypeId(long typeId);

    Page<ProductEntity> findByTypeId(long typeId, Pageable pageable);
}

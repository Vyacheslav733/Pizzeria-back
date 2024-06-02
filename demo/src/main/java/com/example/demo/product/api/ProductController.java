package com.example.demo.product.api;

import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.core.api.PageAttributesMapper;
import com.example.demo.core.configuration.Constants;
import com.example.demo.core.security.UserPrincipal;
import com.example.demo.product.model.ProductEntity;
import com.example.demo.product.service.ProductService;
import com.example.demo.types.api.TypeDto;
import com.example.demo.types.model.TypeEntity;
import com.example.demo.types.service.TypeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(ProductController.URL)
public class ProductController {
    public static final String URL = Constants.ADMIN_PREFIX + "/product";
    public static final String PRODUCT_VIEW = "product";
    public static final String PRODUCT_EDIT_VIEW = "product-edit";
    public static final String PRODUCT_ATTRIBUTE = "product";
    private static final String PAGE_ATTRIBUTE = "page";

    private final ProductService productService;
    private final TypeService typeService;
    private final ModelMapper modelMapper;

    public ProductController(ProductService productService, TypeService typeService,
            ModelMapper modelMapper) {
        this.productService = productService;
        this.typeService = typeService;
        this.modelMapper = modelMapper;
    }

    private ProductDto toDto(ProductEntity entity) {
        return modelMapper.map(entity, ProductDto.class);
    }

    private ExpandedProductDto toExpandedDto(ProductEntity entity, Long userId) {
        ExpandedProductDto dto = modelMapper.map(entity, ExpandedProductDto.class);
        dto.setTypeName(typeService.get(dto.getTypeId()).getName());
        return dto;
    }

    private TypeDto toTypeDto(TypeEntity entity) {
        return modelMapper.map(entity, TypeDto.class);
    }

    private ProductEntity toEntity(ProductDto dto) {
        final ProductEntity entity = modelMapper.map(dto, ProductEntity.class);
        entity.setType(typeService.get(dto.getTypeId()));
        return entity;
    }

    @GetMapping
    public String getAll(
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            Model model,
            @AuthenticationPrincipal UserPrincipal principal) {
        final Map<String, Object> attributes = PageAttributesMapper.toAttributes(
                productService.getAll(0L, page, Constants.DEFAULT_PAGE_SIZE),
                item -> toExpandedDto(item, principal.getId()));
        model.addAllAttributes(attributes);
        model.addAttribute(PAGE_ATTRIBUTE, page);

        return PRODUCT_VIEW;
    }

    @GetMapping("/edit/")
    public String create(Model model) {
        model.addAttribute(PRODUCT_ATTRIBUTE, new ProductDto());
        model.addAttribute("types",
                typeService.getAll().stream()
                        .map(this::toTypeDto)
                        .toList());
        return PRODUCT_EDIT_VIEW;
    }

    @PostMapping("/edit/")
    public String create(
            @ModelAttribute(name = PRODUCT_ATTRIBUTE) @Valid ProductDto product,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return PRODUCT_EDIT_VIEW;
        }

        productService.create(toEntity(product));
        return Constants.REDIRECT_VIEW + URL;
    }

    @GetMapping("/edit/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        model.addAttribute("types",
                typeService.getAll().stream()
                        .map(this::toTypeDto)
                        .toList());
        model.addAttribute(PRODUCT_ATTRIBUTE, toDto(productService.get(id)));
        return PRODUCT_EDIT_VIEW;
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @ModelAttribute(name = PRODUCT_ATTRIBUTE) @Valid ProductDto product,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return PRODUCT_EDIT_VIEW;
        }
        if (id <= 0) {
            throw new IllegalArgumentException();
        }

        productService.update(id, toEntity(product));
        return Constants.REDIRECT_VIEW + URL;
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable(name = "id") Long id) {
        productService.delete(id);
        return Constants.REDIRECT_VIEW + URL;
    }
}

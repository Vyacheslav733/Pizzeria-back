package com.example.demo.types.api;

import java.util.Map;

import org.modelmapper.ModelMapper;
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
import com.example.demo.types.model.TypeEntity;
import com.example.demo.types.service.TypeService;

import jakarta.validation.Valid;

@Controller
@RequestMapping(TypeController.URL)
public class TypeController {
    public static final String URL = Constants.ADMIN_PREFIX + "/type";
    public static final String CATEGORY_VIEW = "type";
    public static final String CATEGORY_EDIT_VIEW = "type-edit";
    public static final String CATEGORY_ATTRIBUTE = "type";
    private static final String PAGE_ATTRIBUTE = "page";

    private final TypeService typeService;
    private final ModelMapper modelMapper;

    public TypeController(TypeService typeService, ModelMapper modelMapper) {
        this.typeService = typeService;
        this.modelMapper = modelMapper;
    }

    private TypeDto toDto(TypeEntity entity) {
        return modelMapper.map(entity, TypeDto.class);
    }

    private TypeEntity toEntity(TypeDto dto) {
        return modelMapper.map(dto, TypeEntity.class);
    }

    @GetMapping
    public String getAll(
            @RequestParam(name = PAGE_ATTRIBUTE, defaultValue = "0") int page,
            Model model) {
        final Map<String, Object> attributes = PageAttributesMapper.toAttributes(
                typeService.getAll(page, Constants.DEFAULT_PAGE_SIZE), this::toDto);
        model.addAllAttributes(attributes);
        model.addAttribute(PAGE_ATTRIBUTE, page);
        return CATEGORY_VIEW;
    }

    @GetMapping("/edit/")
    public String create(Model model) {
        model.addAttribute(CATEGORY_ATTRIBUTE, new TypeDto());
        return CATEGORY_EDIT_VIEW;
    }

    @PostMapping("/edit/")
    public String create(
            @ModelAttribute(name = CATEGORY_ATTRIBUTE) @Valid TypeDto type,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return CATEGORY_EDIT_VIEW;
        }

        typeService.create(toEntity(type));
        return Constants.REDIRECT_VIEW + URL;
    }

    @GetMapping("/edit/{id}")
    public String update(@PathVariable(name = "id") Long id, Model model) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        model.addAttribute(CATEGORY_ATTRIBUTE, toDto(typeService.get(id)));
        return CATEGORY_EDIT_VIEW;
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable(name = "id") Long id,
            @ModelAttribute(name = CATEGORY_ATTRIBUTE) @Valid TypeDto type,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            return CATEGORY_EDIT_VIEW;
        }
        if (id <= 0) {
            throw new IllegalArgumentException();
        }

        typeService.update(id, toEntity(type));
        return Constants.REDIRECT_VIEW + URL;
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable(name = "id") Long id) {
        typeService.delete(id);
        return Constants.REDIRECT_VIEW + URL;
    }
}

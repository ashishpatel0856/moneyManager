package com.ashish.MoneyManager.controller;

import com.ashish.MoneyManager.dto.CategoryDto;
import com.ashish.MoneyManager.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> saveCategory(@RequestBody  CategoryDto categoryDto) {
        CategoryDto savedCategory = categoryService.saveCategory(categoryDto);
        return new ResponseEntity<>(savedCategory,HttpStatus.CREATED);
    }

    @GetMapping("/{type}")
    public ResponseEntity<List<CategoryDto>> getAllCategoriesByTypeForCurrentUser (@PathVariable String type) {
        List<CategoryDto> categoryDtos = categoryService.getAllCategoriesByTypeForCurrentUser(type);
        return new ResponseEntity<>(categoryDtos,HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategoriesForCurrentUser() {
        List<CategoryDto> allCategories = categoryService.getAllCategoriesForCurrentUser();
        return new ResponseEntity<>(allCategories,HttpStatus.OK);
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long categoryId, @RequestBody CategoryDto categoryDto) {
        CategoryDto savedCategory = categoryService.updateCategory(categoryId, categoryDto);
        return new ResponseEntity<>(savedCategory,HttpStatus.OK);

    }
}

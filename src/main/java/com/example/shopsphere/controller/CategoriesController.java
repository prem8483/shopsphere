package com.example.shopsphere.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.shopsphere.entity.Categories;
import com.example.shopsphere.services.CategoriesService;

@RestController
@RequestMapping("/api/categories")
public class CategoriesController {

    @Autowired
    private CategoriesService categoriesService;

    @PostMapping
    public Categories addCategory(@RequestBody Categories category) {
        return categoriesService.addCategories(category);
    }

    @GetMapping
    public List<Categories> getAllCategories() {
        return categoriesService.getAllCategories();
    }

    @GetMapping("/{id}")
    public Categories getCategoryById(@PathVariable Long id) {
        return categoriesService.getCategoriesById(id);
    }

    @PutMapping("/{id}")
    public Categories updateCategory(@PathVariable Long id,
                                     @RequestBody Categories category) {
        return categoriesService.updateCategories(id, category);
    }

    @DeleteMapping("/{id}")
    public String deleteCategory(@PathVariable Long id) {
        categoriesService.deleteCategory(id);
        return "Category deleted successfully";
    }
}

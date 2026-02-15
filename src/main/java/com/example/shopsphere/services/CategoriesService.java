package com.example.shopsphere.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopsphere.entity.Categories;
import com.example.shopsphere.repository.CategoriesRepository;

@Service
public class CategoriesService {

    @Autowired
    private CategoriesRepository categoriesRepository;

    public Categories addCategories(Categories categories) {
        return categoriesRepository.save(categories);
    }

    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    public Categories getCategoriesById(Long id) {
        return categoriesRepository.findById(id).orElseThrow(() -> new RuntimeException("Category not found"));
    }

    public Categories updateCategories(Long id, Categories updatedCategories) {
        Categories categories = getCategoriesById(id);

        categories.setName(updatedCategories.getName());
        categories.setDescription(updatedCategories.getDescription());

        return categoriesRepository.save(categories);
    }

    public void deleteCategory(Long id) {
        categoriesRepository.deleteById(id);
    }
}

package com.example.shopsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopsphere.entity.Categories;

public interface CategoriesRepository extends JpaRepository<Categories, Integer> {

    
}

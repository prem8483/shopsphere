package com.example.shopsphere.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopsphere.entity.Products;
import com.example.shopsphere.services.ProductsService;

@RestController
@RequestMapping("/api/products")
public class ProductsController {
    
    @Autowired
    private ProductsService productsService;

    @PostMapping
    public Products addProducts( @RequestBody Products products ) {
        products.setCreatedAt(LocalDateTime.now());
        return productsService.addProducts(products);
    }

    @GetMapping
    public List<Products> getAllProducts( ) {
        return productsService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Products getProductById( @PathVariable Long id ) {
        return productsService.getProductById(id);
    }

    @PutMapping("/{id}")
    public Products updateProducts( @PathVariable Long id, @RequestBody Products products ) {
        return productsService.updateProducts(id, products);
    }

    @DeleteMapping("/{id}")
    public String deleteProduct( @PathVariable Long id ) {
        productsService.deleteProducts(id);
        return "Product Deleted Successfully";
    }
}

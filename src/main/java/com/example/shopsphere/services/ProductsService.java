package com.example.shopsphere.services;
import com.example.shopsphere.repository.ProductsRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopsphere.entity.Products;

@Service
public class ProductsService {

    @Autowired
    private ProductsRepository productsRepository;

    public Products addProducts( Products products ) {
        return productsRepository.save(products);
    }

    public List<Products> getAllProducts( ) {
        return productsRepository.findAll();
    }

    public Products getProductById( Long id ) {
        return productsRepository.findById(id).orElseThrow (()-> new RuntimeException("Product not found"));
    }

    public Products updateProducts ( Long id, Products updatedProducts ) {

        Products products = getProductById(id);

        products.setName(updatedProducts.getName());
        products.setDescription(updatedProducts.getDescription());
        products.setPrice(updatedProducts.getPrice());
        products.setStock(updatedProducts.getStock());

        return productsRepository.save(products);

    }

    public void deleteProducts( Long id ) {
        productsRepository.deleteById(id);
    }

}

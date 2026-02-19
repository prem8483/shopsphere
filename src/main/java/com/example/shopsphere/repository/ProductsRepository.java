package com.example.shopsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopsphere.entity.Products;

public interface ProductsRepository extends JpaRepository<Products, Long> {
    
    @Modifying
    @Transactional
    @Query("UPDATE Products p set p.stock = p.stock - :quantity"+"WHERE p.id = :productId AND p.stock >= :quantity")
    int decreaseStock( @Param("productId") Long productId, @Param("quantity") int quantity );
}

package com.example.shopsphere.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopsphere.entity.Cart;
import com.example.shopsphere.entity.Users;

public interface CartRepository extends JpaRepository<Cart, Long> {
    
    List<Cart> findByUsers(Users users);
}

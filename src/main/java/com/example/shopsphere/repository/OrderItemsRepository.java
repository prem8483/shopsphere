package com.example.shopsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopsphere.entity.OrderItems;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Integer> {
    
}

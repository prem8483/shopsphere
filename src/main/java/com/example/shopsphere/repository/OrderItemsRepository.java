package com.example.shopsphere.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopsphere.entity.OrderItems;
import com.example.shopsphere.entity.Orders;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
    
    List<OrderItems> findByOrder( Orders order );
}

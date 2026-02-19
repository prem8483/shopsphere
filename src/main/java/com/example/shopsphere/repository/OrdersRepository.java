package com.example.shopsphere.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopsphere.entity.Orders;
import com.example.shopsphere.entity.Users;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
 
    List<Orders> getUserOrders( Users users );

}

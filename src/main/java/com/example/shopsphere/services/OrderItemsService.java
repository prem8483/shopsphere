package com.example.shopsphere.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopsphere.entity.OrderItems;
import com.example.shopsphere.entity.Orders;
import com.example.shopsphere.entity.Users;
import com.example.shopsphere.repository.OrderItemsRepository;
import com.example.shopsphere.repository.OrdersRepository;
import com.example.shopsphere.repository.UsersRepository;

@Service
public class OrderItemsService {
    
    @Autowired
    private OrdersRepository orderRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;


    @Autowired
    private UsersRepository usersRepository;

    public List<OrderItems> getOrderItems( String email, Long orderId ) {

        Users user = usersRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));

        Orders order = orderRepository.findById(orderId).orElseThrow(()-> new RuntimeException("Order not found"));

        if ( !order.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Unauthorized order access");
        }

        return orderItemsRepository.findByOrders( order );
    }
}

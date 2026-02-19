package com.example.shopsphere.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopsphere.dto.OrderRequest;
import com.example.shopsphere.dto.OrderResponse;
import com.example.shopsphere.entity.Orders;
import com.example.shopsphere.services.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;

    @PostMapping
    public Orders placeOrder( Principal principal, @RequestBody OrderRequest orderRequest ) {
        return orderService.placeOrder(principal.getName(), orderRequest.getAddressId(), orderRequest.getPaymentMethod());
    }

    @GetMapping("/history")
    public List<Orders> getUserOrderHistory( Principal principal ) {
        return orderService.getOrderHistory(principal.getName());
    }

    @GetMapping("/{orderId}")
    public OrderResponse getUserOrderDetails( Principal principal, @PathVariable Long orderId ) {
        return orderService.getOrderDetails(orderId, principal.getName());
    }
}

package com.example.shopsphere.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopsphere.dto.OrderRequest;
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
}

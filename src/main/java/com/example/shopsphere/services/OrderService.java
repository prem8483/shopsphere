package com.example.shopsphere.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopsphere.entity.Address;
import com.example.shopsphere.entity.Cart;
import com.example.shopsphere.entity.OrderItems;
import com.example.shopsphere.entity.OrderStatus;
import com.example.shopsphere.entity.Orders;
import com.example.shopsphere.entity.Users;
import com.example.shopsphere.repository.AddressRepository;
import com.example.shopsphere.repository.CartRepository;
import com.example.shopsphere.repository.OrderItemsRepository;
import com.example.shopsphere.repository.OrdersRepository;
import com.example.shopsphere.repository.UsersRepository;

@Service
public class OrderService {
    
    @Autowired
    private OrdersRepository ordersRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Transactional
    public Orders placeOrder( String email, Long addressId, String paymentMethod ) {

        Users users = usersRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));
        List<Cart> cartItems = cartRepository.findByUsers(users);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Address address = addressRepository.findById(addressId).orElseThrow(()-> new RuntimeException("Address not found"));

        if ( !address.getUsers().getUserId().equals(users.getUserId())) {
            throw new RuntimeException("Invalid Address");
        }

        double total = 0;
        for( Cart cart : cartItems ) {
            total += cart.getProducts().getPrice() * cart.getQuantity();
        }

        Orders order = new Orders();
        order.setUsers(users);
        order.setAddress(address);
        order.setTotalPrice(total);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus("SUCCESS");
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        Orders savedOrder = ordersRepository.save(order);

        for( Cart cart : cartItems ) {
            OrderItems orderItems = new OrderItems();
            orderItems.setOrders(savedOrder);
            orderItems.setPrice(cart.getProducts().getPrice());
            orderItems.setProducts(cart.getProducts());
            orderItems.setQuantity(cart.getQuantity());

            orderItemsRepository.save(orderItems);
        }

        cartRepository.deleteAll();
        
        return savedOrder;
    }
}

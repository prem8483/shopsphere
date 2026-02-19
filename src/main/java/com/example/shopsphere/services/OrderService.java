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
import com.example.shopsphere.entity.Products;
import com.example.shopsphere.entity.Users;
import com.example.shopsphere.repository.AddressRepository;
import com.example.shopsphere.repository.CartRepository;
import com.example.shopsphere.repository.OrderItemsRepository;
import com.example.shopsphere.repository.OrdersRepository;
import com.example.shopsphere.repository.ProductsRepository;
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

    @Autowired
    private ProductsRepository productsRepository;

    @Transactional
    public Orders placeOrder(String email, Long addressId, String paymentMethod) {

        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        List<Cart> cartItems = cartRepository.findByUsers(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUsers().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Invalid Address");
        }

        double total = 0;
        for (Cart cart : cartItems) {

            Products product = cart.getProducts();

            if (product.getStock() < cart.getQuantity()) {
                throw new RuntimeException("Not enough stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - cart.getQuantity());
            productsRepository.save(product);
            total += cart.getProducts().getPrice() * cart.getQuantity();
        }

        Orders order = new Orders();
        order.setUser(user);
        order.setAddress(address);
        order.setTotalPrice(total);
        order.setPaymentMethod(paymentMethod);
        order.setPaymentStatus("SUCCESS");
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDateTime.now());

        Orders savedOrder = ordersRepository.save(order);

        for (Cart cart : cartItems) {
            OrderItems orderItems = new OrderItems();
            orderItems.setOrder(savedOrder);
            orderItems.setPrice(cart.getProducts().getPrice());
            orderItems.setProduct(cart.getProducts());
            orderItems.setQuantity(cart.getQuantity());

            orderItemsRepository.save(orderItems);
        }

        cartRepository.deleteAll(cartItems);

        return savedOrder;
    }

    public List<Orders> getUserOrders(String email) {

        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return ordersRepository.getUserOrders(user);
    }

    public Orders getSingleOrders(String email, Long id) {

        Orders order = ordersRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorize user");
        }

        return order;
    }

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    @Transactional
    public Orders updateOrderStatus(String email, Long orderId, OrderStatus orderStatus) {
        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getRole().equals("ADMIN")) {
            throw new RuntimeException("Only admin can update order status");
        }

        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        order.setOrderStatus(orderStatus);

        return ordersRepository.save(order);
    }

    @Transactional
    public Orders cancelOrders(String email, Long orderId) {

        Orders order = ordersRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        if (!order.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Order doesnt belong to the correct user");
        }

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order cannot be cancelled");
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setPaymentStatus("REFUNDED");

        return ordersRepository.save(order);
    }
}

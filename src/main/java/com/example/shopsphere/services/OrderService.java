package com.example.shopsphere.services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.shopsphere.dto.AddressResponse;
import com.example.shopsphere.dto.OrderItemResponse;
import com.example.shopsphere.dto.OrderResponse;
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
        List<Cart> cartItems = cartRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Address address = addressRepository.findById(addressId).orElseThrow(() -> new RuntimeException("Address not found"));

        if (!address.getUser().getUserId().equals(user.getUserId())) {
            throw new RuntimeException("Invalid Address");
        }

        double total = 0;
        for (Cart cart : cartItems) {

            Products product = cart.getProduct();

            int updated = productsRepository.decreaseStock(product.getId(), cart.getQuantity());
            if (updated == 0 ) {
                throw new RuntimeException("Not enough stock for product"+ product.getName());
            }
            total += product.getPrice() * cart.getQuantity();
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
            orderItems.setPrice(cart.getProduct().getPrice());
            orderItems.setProduct(cart.getProduct());
            orderItems.setQuantity(cart.getQuantity());

            orderItemsRepository.save(orderItems);
        }

        cartRepository.deleteAll(cartItems);

        return savedOrder;
    }

    public List<Orders> getUserOrders(String email) {

        Users user = usersRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return ordersRepository.findByUser(user);
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
            throw new RuntimeException("Unauthorized user");
        }

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order cannot be cancelled");
        }


        List<OrderItems> items = orderItemsRepository.findByOrder(order);

        for( OrderItems orderItems : items ) {
            Products product = orderItems.getProduct();
            product.setStock(product.getStock() + orderItems.getQuantity());
            productsRepository.save(product);
        }
        order.setOrderStatus(OrderStatus.CANCELLED);
        order.setPaymentStatus("REFUNDED");

        return ordersRepository.save(order);
    }

    public List<Orders> getOrderHistory( String email ) {
        Users user = usersRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User cannot be found"));

        return ordersRepository.findByUserOrderByOrderDateDesc(user);
    }

    public OrderResponse getOrderDetails( Long orderId, String email ) {
        
        Users user = usersRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));
        Orders order = ordersRepository.findByIdAndUser(orderId, user).orElseThrow(()-> new RuntimeException("Unauthorized User"));

        List<OrderItems> items = orderItemsRepository.findByOrder(order);
        List<OrderItemResponse> orderItemResponses = new ArrayList<>();

        for( OrderItems item : items ) {
            OrderItemResponse dto = new OrderItemResponse();
            dto.setProductId(item.getProduct().getId());
            dto.setPrice(item.getPrice());
            dto.setProductName(item.getProduct().getName());
            dto.setQuantity(item.getQuantity());

            orderItemResponses.add(dto);
        }

        Address address = order.getAddress();
        AddressResponse addressResponse = new AddressResponse();

        addressResponse.setFullName(address.getUser().getName());
        addressResponse.setPincode(address.getPincode());
        addressResponse.setCity(address.getCity());
        addressResponse.setState(address.getState());
        addressResponse.setStreet(address.getStreet());

        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setId(order.getId());
        orderResponse.setAddress(addressResponse);
        orderResponse.setOrderDate(order.getOrderDate());
        orderResponse.setOrderStatus(order.getOrderStatus());
        orderResponse.setPaymentMethod(order.getPaymentMethod());
        orderResponse.setPaymentStatus(order.getPaymentStatus());
        orderResponse.setTotalPrice(order.getTotalPrice());
        orderResponse.setItems(orderItemResponses);

        return orderResponse;

    }
}

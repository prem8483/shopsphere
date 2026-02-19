package com.example.shopsphere.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.example.shopsphere.entity.OrderStatus;

public class OrderResponse {

    private Long id;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private String paymentMethod;
    private String paymentStatus;
    private double totalPrice;
    private AddressResponse address;
    private List<OrderItemResponse> items;

    public OrderResponse () {

    }

    public List<OrderItemResponse> getItems() {
        return items;
    }

    public void setItems(List<OrderItemResponse> items) {
        this.items = items;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }



    public Long getId() {
        return id;
    }



    public void setId(Long id) {
        this.id = id;
    }



    public AddressResponse getAddress() {
        return address;
    }



    public void setAddress(AddressResponse address) {
        this.address = address;
    }   

}

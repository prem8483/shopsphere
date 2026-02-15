package com.example.shopsphere.dto;

public class OrderRequest {
    
    private Long addressId;
    private String paymentMethod;

    public OrderRequest ( ) {

    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    
}

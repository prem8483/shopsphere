package com.example.shopsphere.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopsphere.entity.Cart;
import com.example.shopsphere.services.CartService;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;

    @PostMapping
    public Cart addToCart( Principal principal, @RequestParam Long productId, @RequestParam int quantity) {
        return cartService.addToCart(principal.getName(), productId, quantity);
    }

    @GetMapping
    public List<Cart> getMyCart( Principal principal ) {
        return cartService.getUserCart(principal.getName());
    }

    @DeleteMapping("/{cartId}")
    public String removeFromCart( @PathVariable Long cartId ) {

        cartService.removeFromCart(cartId);
        return "Item removed from cart";
    }
}

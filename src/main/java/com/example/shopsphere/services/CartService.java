package com.example.shopsphere.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.shopsphere.entity.Cart;
import com.example.shopsphere.entity.Products;
import com.example.shopsphere.entity.Users;
import com.example.shopsphere.repository.CartRepository;
import com.example.shopsphere.repository.ProductsRepository;
import com.example.shopsphere.repository.UsersRepository;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private ProductsRepository productsRepository;

    public Cart addToCart ( String email, Long productId, int quantity ) {

        Users users = usersRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User Not Found"));

        Products products = productsRepository.findById(productId).orElseThrow(()-> new RuntimeException("Product Not Found"));

        Cart cart = new Cart();

        cart.setUser(users);
        cart.setProduct(products);
        cart.setQuantity(quantity);

        return cartRepository.save(cart);
    }
    
    public List<Cart> getUserCart( String email ) {

        Users users = usersRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));

        return cartRepository.findByUser(users);
    }

    public void removeFromCart( Long cartId ) {
        cartRepository.deleteById(cartId);
    }
}

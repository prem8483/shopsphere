package com.example.shopsphere.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.shopsphere.entity.Users;
import com.example.shopsphere.repository.UsersRepository;


@Service
public class CustomUserDetailsService implements UserDetailsService {
    
    @Autowired
    UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername( String email ) throws UsernameNotFoundException {

        Users users = usersRepository.findByEmail( email )
            .orElseThrow(()-> new UsernameNotFoundException("User Not Found"));
        
        return new User(
            users.getEmail(),
            users.getPassword(),
            Collections.emptyList()
        );
    }
}

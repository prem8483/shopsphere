package com.example.shopsphere.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.shopsphere.entity.Users;
import com.example.shopsphere.repository.UsersRepository;

@Service
public class UserService {
    
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users savedUsers( Users users ) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setTimeStamp(LocalDateTime.now());
        return usersRepository.save(users);
    }
}

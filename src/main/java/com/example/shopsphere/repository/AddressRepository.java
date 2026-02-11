package com.example.shopsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopsphere.entity.Address;

public interface AddressRepository extends JpaRepository<Address, Integer> {
    
}

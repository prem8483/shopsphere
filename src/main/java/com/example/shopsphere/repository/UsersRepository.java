package com.example.shopsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopsphere.entity.Users;

public interface UsersRepository extends JpaRepository<Users, Integer> {

}

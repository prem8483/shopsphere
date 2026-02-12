package com.example.shopsphere.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.shopsphere.entity.Reviews;

public interface ReviewsRepository extends JpaRepository<Reviews, Long>{
    
}

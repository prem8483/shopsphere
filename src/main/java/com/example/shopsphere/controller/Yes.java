package com.example.shopsphere.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Yes {
    
    @GetMapping("/test")
    public String test() {
        return "Protected API Working";
    }
    
}

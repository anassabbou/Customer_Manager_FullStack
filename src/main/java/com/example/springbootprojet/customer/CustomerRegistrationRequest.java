package com.example.springbootprojet.customer;

public record CustomerRegistrationRequest(
        String name,
        String email,
        Integer age
){}


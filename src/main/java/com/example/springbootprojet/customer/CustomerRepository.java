package com.example.springbootprojet.customer;

import org.springframework.data.jpa.repository.JpaRepository;
// JpaRepository<Customer, id:Integer>
public interface CustomerRepository extends JpaRepository<Customer, Integer> {



}

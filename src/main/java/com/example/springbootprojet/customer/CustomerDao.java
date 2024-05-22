package com.example.springbootprojet.customer;

import com.example.springbootprojet.customer.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerDao {
    public List<Customer> selectAllCustomers();
    public Optional<Customer> selectCustomerById(Integer id);

}

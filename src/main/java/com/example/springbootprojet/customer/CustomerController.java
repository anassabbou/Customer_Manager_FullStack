package com.example.springbootprojet.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@RestController
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("api/v1/customers")
    public List<Customer> GetCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("api/v1/customer/{id}")
    public Customer GetCustomerById(@PathVariable(value = "id") Integer id){
        return customerService.getCustomer(id);
    }

}

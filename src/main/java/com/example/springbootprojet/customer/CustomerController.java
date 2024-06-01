package com.example.springbootprojet.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {
    private final CustomerService customerService;
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public List<Customer> getCustomers(){
        return customerService.getAllCustomers();
    }

    @GetMapping("{id}")
    public Customer getCustomerById(@PathVariable(value = "id") Integer id){
        return customerService.getCustomer(id);
    }
    @PostMapping
    public void registerCustomer(@RequestBody CustomerRegistrationRequest request){
        customerService.addCustomer(request);
    }
    @DeleteMapping("{id}")
    public void deleteCustomer(@PathVariable(value = "id") Integer customerId){
        customerService.deleteCustomerByID(customerId);
    }

    @PutMapping("{customerId}")
    public void updateCustomer(@PathVariable(value="customerId") Integer customerId,
                               @RequestBody CustomerRegistrationRequest request){
        customerService.updateCustomer(customerId, request);
    }

}

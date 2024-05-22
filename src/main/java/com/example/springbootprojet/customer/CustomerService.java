package com.example.springbootprojet.customer;

import com.example.springbootprojet.exception.ResourceNotFound;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
//@Component
@Service //class for business logic
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id){
        return customerDao.selectCustomerById(id).orElseThrow(
                ()-> new ResourceNotFound("customer [%s] id not found".formatted(id))
        );
    }
}

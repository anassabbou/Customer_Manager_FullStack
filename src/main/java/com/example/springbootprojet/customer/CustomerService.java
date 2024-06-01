package com.example.springbootprojet.customer;

import com.example.springbootprojet.exception.DuplicateResourceException;
import com.example.springbootprojet.exception.RequestValidationException;
import com.example.springbootprojet.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

//@Component
@Service //class for business logic
public class CustomerService {
    private final CustomerDao customerDao;

    // inject CustomerDao
    public CustomerService(@Qualifier("jpa") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id){
        return customerDao.selectCustomerById(id).orElseThrow(
                ()-> new ResourceNotFoundException("customer [%s] id not found".formatted(id))
        );
    }

    public void addCustomer(CustomerRegistrationRequest customerRegistrationRequest){
        // check email
        if(customerDao.existsPersonWithEmail(customerRegistrationRequest.email())){
            throw new DuplicateResourceException("Email already taken");
        }
        // add
        customerDao.insertCustomer(new Customer(
                customerRegistrationRequest.name(),
                customerRegistrationRequest.email(),
                customerRegistrationRequest.age()
        ));

    }

    public void deleteCustomerByID(Integer id){

        if(!customerDao.existsPersonWithId(id)) {
            throw new ResourceNotFoundException("customer with id [%s] not found!".formatted(id));
        }
        customerDao.deleteCustomerById(id);

    }


    //*** business logic
    public void updateCustomer(Integer id, CustomerRegistrationRequest updateRequest){
        Customer customer= getCustomer(id);
        boolean changes=false;
        if(updateRequest.name() !=null && !updateRequest.name().equals(customer.name)){
            customer.setName(updateRequest.name());
            changes=true;
        }
        if(updateRequest.age() !=null && !updateRequest.age().equals(customer.age)){
            customer.setAge(updateRequest.age());
            changes=true;
        }
        if(updateRequest.email() !=null && !updateRequest.email().equals(customer.email)){
            if(customerDao.existsPersonWithEmail(updateRequest.email())){
                throw new DuplicateResourceException(
                        "Email already taken"
                );
            }
            customer.setEmail(updateRequest.email());
            changes=true;
        }
        if(!changes){
            throw new RequestValidationException("no data changes found");
        }
        // invoke
        customerDao.updateCustomer(customer);

    }


}

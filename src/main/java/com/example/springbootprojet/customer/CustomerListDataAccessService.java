package com.example.springbootprojet.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{

    //DB
    static List<Customer> customers;
    static {
        customers=new ArrayList<>();
        Customer anass= new Customer(1,"anass","anas@gmail.com",23);
        Customer khalid= new Customer(2,"khalid","khalid@gmail.com",22);
        try{
            customers.add(anass);
            customers.add(khalid);
        }catch (NullPointerException e){
            e.fillInStackTrace();
        }

    }
    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
         return customers.stream()
                 .filter(customer -> (customer.getId()).equals(id))
                 .findFirst();

    }


}

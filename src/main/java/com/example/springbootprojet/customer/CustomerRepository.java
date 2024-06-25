package com.example.springbootprojet.customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

// JpaRepository<Customer, id:Integer>
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    // JPQL : creating sql query by using jpa method
    // @Query("select c from Customer Where ....")
    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Integer id);


}

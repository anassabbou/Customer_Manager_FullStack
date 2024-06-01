package com.example.springbootprojet.customer;

import org.springframework.data.jpa.repository.JpaRepository;
// JpaRepository<Customer, id:Integer>
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    // JPQL : creating sql query by using jpa method
    boolean existsCustomerByEmail(String email);
    boolean existsCustomerById(Integer id);


}

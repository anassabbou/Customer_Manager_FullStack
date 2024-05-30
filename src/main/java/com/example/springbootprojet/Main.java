package com.example.springbootprojet;

import com.example.springbootprojet.customer.Customer;
import com.example.springbootprojet.customer.CustomerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean // spring create instance of CommandLineRunner
    CommandLineRunner runner(CustomerRepository customerRepository){
        return args -> {
            Customer anass= new Customer("anass","anas@gmail.com",23);
            Customer khalid= new Customer("khalid","khalid@gmail.com",22);
            List<Customer> customerList= List.of(anass,khalid);
            customerRepository.saveAll(customerList);
        };
    }
}

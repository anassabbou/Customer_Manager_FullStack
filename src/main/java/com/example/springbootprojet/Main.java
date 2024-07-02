package com.example.springbootprojet;
import com.example.springbootprojet.customer.Customer;
import com.example.springbootprojet.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean // spring create instance of CommandLineRunner
    CommandLineRunner runner(CustomerRepository customerRepository){
        return args -> {
            Faker faker=new Faker();
            Random rAge=new Random();
            Customer customer=new Customer(
              faker.name().fullName(),
              faker.internet().emailAddress(),
              rAge.nextInt(18,60)
            );
            // Use this with DDL (hibernates)
            customerRepository.save(customer);
        };
    }
}

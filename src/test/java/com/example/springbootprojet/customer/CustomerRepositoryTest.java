package com.example.springbootprojet.customer;

import com.example.springbootprojet.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
@DataJpaTest
// disable embedded database
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CustomerRepositoryTest extends AbstractTestContainers {
    @Autowired
    private CustomerRepository underTest;
    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
        System.out.println(applicationContext.getBeanDefinitionCount());
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        Customer customer=new Customer(

                FAKER.name().fullName(),
                FAKER.internet().emailAddress() + "-"+ UUID.randomUUID(),
                20
        );// id null
        underTest.save(customer);
        // When
        boolean actual= underTest.existsCustomerByEmail(customer.email);

        // Then
        assertThat(actual).isTrue();

    }

    @Test
    void existsCustomerById() {
        // Given
        Customer customer=new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress() + "-"+ UUID.randomUUID(),
                20
        );
        underTest.save(customer);
        int id= underTest.findAll()
                .stream()
                .filter(c->c.getEmail().equals(customer.email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        var actual= underTest.existsCustomerById(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerByEmailFailsWhenEmailNotPresent(){
        // Given
           String email= FAKER.internet().emailAddress() + "-"+ UUID.randomUUID();
        // When
        boolean actual= underTest.existsCustomerByEmail(email);

        // Then
        assertThat(actual).isFalse();

    }

    @Test
    void existsCustomerByIdFailsWhenIdNotPresent() {
        // Given
        int id=-1;

        // When
        var actual= underTest.existsCustomerById(id);

        // Then
        assertThat(actual).isFalse();
        // Then

    }
}
package com.example.springbootprojet.customer;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

class CustomerJPADataAccessServiceTest {
    private CustomerJPADataAccessService underTest;
    @Mock private CustomerRepository customerRepository;
    private AutoCloseable autoCloseable;// for fresh Mocks
    private final Faker faker=new Faker();

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest= new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception{
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        // Given: Method not accept anything
        // When: invoke my method (selectAllCustomers)
            underTest.selectAllCustomers();
        // Then
            verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        // Given
        int id=1;
        // When
        underTest.selectCustomerById(id);
        // Then
        verify(customerRepository).findById(id);
    }

    @Test
    void insertCustomer() {
        // Given
            Customer customer= new Customer(
                    faker.name().fullName(),
                    faker.internet().emailAddress(),
                    23
            );
        // When
            underTest.insertCustomer(customer);

        // Then
        verify(customerRepository).save(customer);

    }

    @Test
    void existsPersonWithEmail() {
        // Given
        String email= faker.internet().emailAddress();
        // When
            underTest.existsPersonWithEmail(email);
        // Then
            verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existsPersonWithId() {
        // Given
            int id=1;
        // When
            underTest.existsPersonWithId(id);
        // Then
            verify(customerRepository).existsById(id);
    }

    @Test
    void deleteCustomerById() {
        // Given
            int id=1;
        // When
            underTest.deleteCustomerById(id);
        // Then
            verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        // Given
            Customer update=new Customer(
                    1,
                    faker.name().fullName(),
                    faker.internet().emailAddress(),
                    25
            );
        // When
            underTest.updateCustomer(update);
        // Then
verify(customerRepository).save(update);
    }
}
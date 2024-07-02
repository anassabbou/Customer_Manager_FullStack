package com.example.springbootprojet.customer;

import com.example.springbootprojet.exception.DuplicateResourceException;
import com.example.springbootprojet.exception.RequestValidationException;
import com.example.springbootprojet.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

// this @ExtendWith have some work of MockAnnotation.openMocks() and
// closeable for it, and more
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        underTest=new CustomerService(customerDao);
    }


    @Test
    void getAllCustomers() {
        // When
        underTest.getAllCustomers();
        // Then
        Mockito.verify(customerDao).selectAllCustomers();

    }

    @Test
    void canGetCustomer() {
        // Given
            int id = 8;
            Customer customer=new Customer(
                    id,"anass","anass@gmail.com",24,
                    Gender.MALE);
        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.of(customer));
        // When
            Customer actual= underTest.getCustomer(id);
        // Then
            assertThat(actual).isEqualTo(customer);
    }

    @Test
    void willThrowWhenGetCustomerReturnEmptyOptional() {
        // Given
        int id = 8;

        when(customerDao.selectCustomerById(id))
                .thenReturn(Optional.empty());
        // When
        // Then
assertThatThrownBy(()->underTest.getCustomer(id))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage("customer [%s] id not found".formatted(id));
    }

    @Test
    void addCustomer() {
        // Given
            String email="anass@gmail.com";
            when(customerDao.existsPersonWithEmail(email)).thenReturn(false);
            CustomerRegistrationRequest request=new CustomerRegistrationRequest("anass","anass@gmail.com",24, Gender.MALE);
        // When
            underTest.addCustomer(request);
        // Then
            ArgumentCaptor<Customer> customerArgumentCaptor= ArgumentCaptor.forClass(Customer.class);
            verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

            Customer cupturedCustomer= customerArgumentCaptor.getValue();
            assertThat(cupturedCustomer.getId()).isEqualTo(null);
            assertThat(cupturedCustomer.getName()).isEqualTo(request.name());
            assertThat(cupturedCustomer.getEmail()).isEqualTo(request.email());
            assertThat(cupturedCustomer.getAge()).isEqualTo(request.age());
    }
    @Test
    void WillThrowWhenEmailExistWhileAddingACustomer() {
        // Given
        String email="anass@gmail.com";
        when(customerDao.existsPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request=new CustomerRegistrationRequest("anass",email,24, Gender.MALE);
        // When
        assertThatThrownBy(()->underTest.addCustomer(request)).isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email already taken");
        // Then
        verify(customerDao, never()).insertCustomer(any());

    }

    @Test
    void deleteCustomerByID() {
        // Given
        int id=1;
        when(customerDao.existsPersonWithId(id)).thenReturn(true);
        // When
        underTest.deleteCustomerByID(id);
        // Then
        verify(customerDao).deleteCustomerById(id);

    }
    @Test
    void WillThrowWhenIdNotExistWhileDeletingCustomerByID() {
        // Given
        int id=1;
        when(customerDao.existsPersonWithId(id)).thenReturn(false);
        // When
        assertThatThrownBy(()->underTest.deleteCustomerByID(id)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found!".formatted(id));
        // Then
        verify(customerDao, never()).deleteCustomerById(id);

    }


    @Test
    void canUpdateAllCustomerProperties() {
        // Given
            int id=1;
            Customer customer=new Customer(
                    id,
                    "anass",
                    "anass@gmail.com",
                    24,
                    Gender.MALE);
            String newEmail="abbou@gmail.com";
            when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
            when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);
            CustomerRegistrationRequest updateRequest= new CustomerRegistrationRequest("abbou",newEmail,26, Gender.MALE);
        // When
            underTest.updateCustomer(id,updateRequest);
        // Then
            ArgumentCaptor<Customer> argumentCaptor= ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(argumentCaptor.capture());

        Customer captoredCustomer= argumentCaptor.getValue();
        assertThat(captoredCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(captoredCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(captoredCustomer.getEmail()).isEqualTo(updateRequest.email());


    }

    @Test
    void canUpdateOnlyCustomerName() {
        // Given
        int id=1;
        Customer customer=new Customer(
                id,
                "anass",
                "anass@gmail.com",
                24,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerRegistrationRequest updateRequest= new CustomerRegistrationRequest("abbou",null,null, Gender.MALE);
        // When
        underTest.updateCustomer(id,updateRequest);
        // Then
        ArgumentCaptor<Customer> argumentCaptor= ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(argumentCaptor.capture());

        Customer captoredCustomer= argumentCaptor.getValue();
        assertThat(captoredCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(captoredCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(captoredCustomer.getEmail()).isEqualTo(customer.getEmail());


    }

    @Test
    void canUpdateOnlyCustomerEmail() {
        // Given
        int id=1;
        Customer customer=new Customer(
                id,
                "anass",
                "anass@gmail.com",
                24,
                Gender.MALE);
        String newEmail="abbou@gmail.com";
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(false);
        CustomerRegistrationRequest updateRequest= new CustomerRegistrationRequest(null,newEmail,null, Gender.MALE);
        // When
        underTest.updateCustomer(id,updateRequest);
        // Then
        ArgumentCaptor<Customer> argumentCaptor= ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(argumentCaptor.capture());

        Customer captoredCustomer= argumentCaptor.getValue();
        assertThat(captoredCustomer.getAge()).isEqualTo(customer.getAge());
        assertThat(captoredCustomer.getName()).isEqualTo(customer.getName());
        assertThat(captoredCustomer.getEmail()).isEqualTo(newEmail);


    }

    @Test
    void WillThrowWhenTryingUpdateCustomerEmailWhenAlreadyTaken() {
        // Given
        int id=1;

        Customer customer=new Customer(id, "anass", "anass@gmail.com", 24, Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        String newEmail="abbou@gmail.com";
        CustomerRegistrationRequest updateRequest= new CustomerRegistrationRequest(null,newEmail,null, Gender.MALE);
        when(customerDao.existsPersonWithEmail(newEmail)).thenReturn(true);

        // When
            assertThatThrownBy(()->underTest.updateCustomer(id,updateRequest))
                    .isInstanceOf(DuplicateResourceException.class)
                    .hasMessage("Email already taken");
        // Then
        verify(customerDao, never()).updateCustomer(any());


    }

    @Test
    void canUpdateOnlyCustomerAge() {
        // Given
        int id=1;
        Customer customer=new Customer(
                id,
                "anass",
                "anass@gmail.com",
                24,
                Gender.MALE);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerRegistrationRequest updateRequest= new CustomerRegistrationRequest(null,null,26, Gender.MALE);
        // When
        underTest.updateCustomer(id,updateRequest);
        // Then
        ArgumentCaptor<Customer> argumentCaptor= ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(argumentCaptor.capture());

        Customer captoredCustomer= argumentCaptor.getValue();
        assertThat(captoredCustomer.getAge()).isEqualTo(updateRequest.age());
        assertThat(captoredCustomer.getName()).isEqualTo(customer.getName());
        assertThat(captoredCustomer.getEmail()).isEqualTo(customer.getEmail());


    }

    @Test
    void WillThrowWhenCustomerUpdateHasNoChanges() {
        // Given
        int id=1;
        Customer customer=new Customer(
                id,
                "anass",
                "anass@gmail.com",
                24,
                Gender.MALE);

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));
        CustomerRegistrationRequest updateRequest= new CustomerRegistrationRequest(
                customer.getName(),
                customer.getEmail(),
                customer.getAge(),
                customer.getGender()
        );
        // When
assertThatThrownBy(()->underTest.updateCustomer(id,updateRequest))
        .isInstanceOf(RequestValidationException.class)
        .hasMessage("no data changes found");
        // Then
        verify(customerDao, never()).updateCustomer(any());



    }



}
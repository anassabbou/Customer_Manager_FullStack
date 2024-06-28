package com.example.springbootprojet.customer;

import com.example.springbootprojet.AbstractTestContainers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestContainers {
    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper= new CustomerRowMapper();

    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                customerRowMapper,
                getJdbcTemplate());
    }

    @Test
    void selectAllCustomers() {
        // Given
            Customer customer=new Customer(
                    FAKER.name().fullName(),
                    FAKER.internet().emailAddress() + "-"+ UUID.randomUUID(),
                    20
            );
            underTest.insertCustomer(customer);
        // When
            List<Customer> actual= underTest.selectAllCustomers();
        // Then
            assertThat(actual).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
        // Given
        Customer customer=new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress() + "-"+ UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        int customerId= underTest.selectAllCustomers().stream()
                .filter(c->c.getEmail().equals(customer.email)).map(Customer::getId)
                .findFirst().orElseThrow();
        // When
        Optional<Customer> actual= underTest.selectCustomerById(customerId);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(customerId);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });

    }

    @Test
    void willReturnEmptyWhenSelectCustomerById() {
        // Given
            //int id= 1;// test failed because::this id is taken by another customer
        int id= -1;// we want to set value not exist
        // When
            var actual= underTest.selectCustomerById(id);
        // Then
            assertThat(actual).isEmpty();
    }


    @Test
    void existPersonWithEmail() {
        // Given
        Customer customer=new Customer(

                FAKER.name().fullName(),
                FAKER.internet().emailAddress() + "-"+ UUID.randomUUID(),
                20
        );// id null
        underTest.insertCustomer(customer);
        // When
        boolean actual= underTest.existsPersonWithEmail(customer.email);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsPersonWithEmailReturnsFalseWhenNotExists() {
        // Given
String email= FAKER.internet().emailAddress();
        // When
boolean actual= underTest.existsPersonWithEmail(email);
        // Then
assertThat(actual).isFalse();
    }

    @Test
    void existPersonWithId() {
        // Given
        Customer customer=new Customer(
                FAKER.name().fullName(),
                FAKER.internet().emailAddress() + "-"+ UUID.randomUUID(),
                20
        );
        underTest.insertCustomer(customer);
        int id= underTest.selectAllCustomers()
                .stream()
                .filter(c->c.getEmail().equals(customer.email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();
        // When
        var actual= underTest.existsPersonWithId(id);

        // Then
        assertThat(actual).isTrue();
    }

    @Test
    void existsCustomerWithIdWillReturnFalseWhenIdNotCustomer() {
        // Given
        int id=-1;

        // When
var actual = underTest.existsPersonWithId(id);
        // Then
assertThat(actual).isFalse();
    }

    @Test
    void deleteCustomerById() {
        // Given
        String email= FAKER.internet().emailAddress() + "-"+ UUID.randomUUID();
        Customer customer=new Customer(email,
                FAKER.name().fullName(),
                20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail()
                .equals(customer.email)).map(Customer::getId)
                .findFirst().orElseThrow();


        // When
        underTest.deleteCustomerById(id);
        // Then
        Optional<Customer> actual= underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();


    }

    @Test
    void updateCustomer() {
        // Given
        String email= FAKER.internet().emailAddress() + "-"+ UUID.randomUUID();
        Customer customer=new Customer(email,
                FAKER.name().fullName(),
                20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail()
                        .equals(customer.email)).map(Customer::getId)
                .findFirst().orElseThrow();

        var newName= "testName";

        // When age is name
        Customer update= new Customer();
        update.setId(id);
        update.setName(newName);

        underTest.updateCustomer(update);

    //Then
        Optional<Customer> actual= underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(newName);
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());

        });


    }

    @Test
    void updateCustomerEmail() {
        // Given
        String email= FAKER.internet().emailAddress() + "-"+ UUID.randomUUID();
        Customer customer=new Customer(email,
                FAKER.name().fullName(),
                20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail()
                        .equals(customer.email)).map(Customer::getId)
                .findFirst().orElseThrow();

        var newEmail= FAKER.internet().emailAddress() + "-"+ UUID.randomUUID();

        // When email is changed
        Customer update= new Customer();
        update.setId(id);
        update.setEmail(newEmail);
        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual= underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.name);
            assertThat(c.getEmail()).isEqualTo(newEmail); // changed
            assertThat(c.getAge()).isEqualTo(customer.getAge());

        });
    }

    @Test
    void updateCustomerAge() {
        // Given
        String email= FAKER.internet().emailAddress() + "-"+ UUID.randomUUID();
        Customer customer=new Customer(email,
                FAKER.name().fullName(),
                20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail()
                        .equals(customer.email)).map(Customer::getId)
                .findFirst().orElseThrow();

        var newAge= 90;

        // When email is changed
        Customer update= new Customer();
        update.setId(id);
        update.setAge(newAge);
        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual= underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.name);
            assertThat(c.getEmail()).isEqualTo(customer.email);
            assertThat(c.getAge()).isEqualTo(newAge);// change

        });
    }

    @Test
    void willUpdateAllPropertiesCustomer() {
        // Given
        String email= FAKER.internet().emailAddress() + "-"+ UUID.randomUUID();
        Customer customer=new Customer(email,
                FAKER.name().fullName(),
                20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail()
                        .equals(customer.email)).map(Customer::getId)
                .findFirst().orElseThrow();

        // When update with new name, age and email
        Customer update= new Customer();
        update.setId(id);
        update.setName("foo");
        update.setEmail(UUID.randomUUID().toString());
        update.setAge(26);

        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual= underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValue(update);
    }

    @Test
    void willNotUpdateWhenNothingToUpdate() {
        // Given
        String email= FAKER.internet().emailAddress() + "-"+ UUID.randomUUID();
        Customer customer=new Customer(email,
                FAKER.name().fullName(),
                20);
        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers().stream()
                .filter(customer1 -> customer1.getEmail()
                        .equals(customer.email)).map(Customer::getId)
                .findFirst().orElseThrow();

        // When update without no changes
        Customer update= new Customer();
        update.setId(id);

        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual= underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValueSatisfying(c->{
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.name);
            assertThat(c.getEmail()).isEqualTo(customer.email);
            assertThat(c.getAge()).isEqualTo(customer.getAge());

        });
    }


}
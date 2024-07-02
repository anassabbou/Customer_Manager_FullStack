package com.example.springbootprojet.journey;

import com.example.springbootprojet.customer.Customer;
import com.example.springbootprojet.customer.CustomerRegistrationRequest;
import com.example.springbootprojet.customer.CustomerUpdateRequest;
import com.example.springbootprojet.customer.Gender;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {
    @Autowired
    private WebTestClient webTestClient;

    //      @Autowired
    //      private CustomerController // don't do this

    @Test
    void canRegisterACustomer() {
    // create registration request
        Faker FAKER= new Faker();
        int age=new Random().nextInt(18,40);
        String name=FAKER.name().fullName();
        String email=FAKER.name().lastName() + UUID.randomUUID() + "@gmail.com";
        Gender gender= age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest request= new CustomerRegistrationRequest(
                name,
                email,
                age,
                gender
        );
    // send a post request
        webTestClient.post().uri("api/v1/customers")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();
        // get all customers
            List<Customer> allCustomers=webTestClient.get().uri("api/v1/customers").accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                    .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                    .returnResult()
                    .getResponseBody();

        // make sure that customer is present
            Customer expectedCustomer= new Customer(name,email,age, Gender.MALE);
            assertThat(allCustomers)
                    .usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                    .contains(expectedCustomer);
        // get customer by id
        int id= allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();
        expectedCustomer.setId(id);
        webTestClient.get().uri("api/v1/customers"+"/{id}", id).accept(MediaType.APPLICATION_JSON).exchange().expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);

    }

    @Test
    void canDeleteCustomer() {
        // create registration request
            Faker FAKER= new Faker();
            int age=new Random().nextInt(18,40);
            String name=FAKER.name().fullName();
            String email=FAKER.name().lastName() + UUID.randomUUID() + "@gmail.com";
            Gender gender= age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
            CustomerRegistrationRequest request= new CustomerRegistrationRequest(name, email, age, gender);
        // send a post request
            webTestClient.post().uri("api/v1/customers")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(request), CustomerRegistrationRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk();
        // get all customers
            List<Customer> allCustomers=webTestClient.get().uri("api/v1/customers").accept(MediaType.APPLICATION_JSON)
                    .exchange().expectStatus().isOk()
                    .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                    .returnResult()
                    .getResponseBody();

        int id= allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();
        // delete customer

        webTestClient.delete().uri("api/v1/customers/{id}",id).accept(MediaType.APPLICATION_JSON).exchange()
                .expectStatus().isOk();

        // get customer by id


        webTestClient.get().uri("api/v1/customers"+"/{id}", id).accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {
        // create registration request
            Faker FAKER= new Faker();
            int age=new Random().nextInt(18,40);
            String name=FAKER.name().fullName();
            String email=FAKER.name().lastName() + UUID.randomUUID() + "@gmail.com";
            Gender gender= age % 2 == 0 ? Gender.MALE : Gender.FEMALE;
            CustomerRegistrationRequest request= new CustomerRegistrationRequest(
                    name,
                    email,
                    age,
                    gender
            );
        // send a post request
            webTestClient.post().uri("api/v1/customers")
                    .accept(MediaType.APPLICATION_JSON)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Mono.just(request), CustomerRegistrationRequest.class)
                    .exchange()
                    .expectStatus()
                    .isOk();
        // get all customers
            List<Customer> allCustomers=webTestClient.get().uri("api/v1/customers").accept(MediaType.APPLICATION_JSON)
                    .exchange().expectStatus().isOk()
                    .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                    .returnResult()
                    .getResponseBody();

            int id= allCustomers.stream()
                .filter(customer -> customer.getEmail().equals(email))
                .map(Customer::getId).findFirst().orElseThrow();

        // update customer
        String newName="khalid";
        CustomerUpdateRequest updateRequest= new CustomerUpdateRequest(newName,null,null);

        webTestClient.put().uri("api/v1/customers/{id}",id).accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get customer by id


        Customer updatedCustomer= webTestClient.get().uri("api/v1/customers"+"/{id}", id).accept(MediaType.APPLICATION_JSON)
                .exchange().expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        Customer expectedUpdate= new Customer(id,newName,email,age, gender);
        assertThat(updatedCustomer).isEqualTo(expectedUpdate);
    }

}

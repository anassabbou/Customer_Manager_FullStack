package com.example.springbootprojet.customer;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString @EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Customer {

    // for mapping this class in table database
    @Id
    @SequenceGenerator(name = "customer_id_sequence", sequenceName = "customer_id_sequence")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_sequence")
    @Column(nullable = false)
    public Integer id;
    @Column(
            nullable = false
    )
    public String name;
    @Column(
            nullable = false
    )
    public String email;
    @Column(
            nullable = false
    )
    public Integer age;


    public Customer(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }

}

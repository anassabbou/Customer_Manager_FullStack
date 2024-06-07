package com.example.springbootprojet.customer;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.relational.core.sql.In;

import java.math.BigInteger;


@Entity
@Table(
        name = "customer",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "customer_email_unique",
                        columnNames = "email"
                )
        }
)
@Getter @Setter @ToString @EqualsAndHashCode @AllArgsConstructor @NoArgsConstructor
public class Customer {

    // for mapping this class in table database
    @Id
    @SequenceGenerator(
            name = "customer_id_seq",
            sequenceName = "customer_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_id_seq"
    )
    @Column(columnDefinition = "BIGSERIAL")
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

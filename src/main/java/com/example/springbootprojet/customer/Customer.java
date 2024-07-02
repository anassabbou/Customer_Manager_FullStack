package com.example.springbootprojet.customer;

import jakarta.persistence.*;
import lombok.*;


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
@Getter @Setter @ToString @EqualsAndHashCode @NoArgsConstructor
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
    private Integer id;
    @Column(
            nullable = false
    )
    private String name;
    @Column(
            nullable = false
    )
    private String email;
    @Column(
            nullable = false
    )
    private Integer age;
    // for example
    @Column(
            nullable = false
    )
    @Enumerated(EnumType.STRING)
    private Gender gender;


    public Customer(Integer id, String name, String email, Integer age, Gender gender) {
        this.id=id;
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender=gender;
    }
    public Customer(String name, String email, Integer age, Gender gender) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.gender=gender;
    }

}

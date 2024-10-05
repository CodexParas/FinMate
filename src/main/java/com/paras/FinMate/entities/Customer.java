package com.paras.FinMate.entities;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.CreatedDate;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    @Column(unique = true, nullable = false)
    private String accountNumber;
    @Column(unique = true, nullable = false)
    private String phoneNumber;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Timestamp createdDate;

    @PrePersist
    public void prePersist () {
        if (this.accountNumber == null) {
            this.accountNumber = RandomStringUtils.randomAlphanumeric(10);
        }
    }

    public String getFullName () {
        return firstName + " " + lastName;
    }

}

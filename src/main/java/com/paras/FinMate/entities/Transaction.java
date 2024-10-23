package com.paras.FinMate.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "transaction")
@EntityListeners(AuditingEntityListener.class)
public class Transaction {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    @JsonIgnore
    private Customer customer;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    @Column(nullable = false, precision = 15)
    private Double amount;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Currency currency;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    private String swiftCode;

    private String transactionDate;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Timestamp createdDate;

    public static TransactionType getTransactionType (String transactionType) {
        return TransactionType.valueOf(transactionType.toUpperCase());
    }

    public static Currency getCurrency (String currency) {
        return Currency.valueOf(currency.toUpperCase());
    }

    @JsonProperty("customer_name")
    public String getCustomerId () {
        return customer.getFullName();
    }

    @PrePersist
    public void prePersist () {
        if (this.id == null) {
            this.id = generateTransactionId();
        }
        if (this.status == null) {
            // Set random Status
            this.status = Status.values()[(int) (Math.random() * Status.values().length)];
        }
        if (this.transactionDate == null) {
            this.transactionDate = String.valueOf(LocalDate.now());
        }
    }

    private String generateTransactionId () {
        return "FINM" + RandomStringUtils.randomNumeric(6);
    }

    private enum TransactionType {
        DOMESTIC,
        INTERNATIONAL
    }

    private enum Currency {
        INR,
        USD,
        EUR,
        GBP
    }

    private enum Status {
        PENDING,
        SUCCESS,
        FAILED,
        REVERSED
    }


}

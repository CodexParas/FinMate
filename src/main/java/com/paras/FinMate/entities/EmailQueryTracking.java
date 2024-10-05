package com.paras.FinMate.entities;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "email_query_tracking")
public class EmailQueryTracking {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String emailSubject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String emailBody;

    @Column(nullable = false)
    private String threadId;

    @Column(nullable = false)
    private String queryType;

    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Column(nullable = false)
    private Integer attemptCount = 0;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ResolutionStatus resolutionStatus;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Timestamp createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private Timestamp lastModifiedDate;

    @PrePersist
    private void prePersist() {
        if (this.id == null) {
            this.id = generateEmailQueryTrackingId();
        }
    }

    private String generateEmailQueryTrackingId() {
        return "EQT" + RandomStringUtils.randomNumeric(7);
    }

    public enum ResolutionStatus {
        PENDING,
        WIP,
        RESOLVED
    }
}

package com.paras.FinMate.entities;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ticket")
@EntityListeners(AuditingEntityListener.class)
public class Ticket {

    @Id
    private String id;

    @OneToOne
    @JoinColumn(name = "query_id", nullable = false)
    private EmailQueryTracking query;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus ticketStatus;

    private String assignedAgent;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    private Timestamp createdDate;

    @LastModifiedDate
    @Column(nullable = false)
    private Timestamp lastModifiedDate;

    @PrePersist
    private void prePersist () {
        if (this.id == null) {
            this.id = generateTicketId();
        }
    }

    private String generateTicketId () {
        return "TICK" + RandomStringUtils.randomNumeric(6);
    }

    private enum TicketStatus {
        OPEN,
        WIP,
        RESOLVED
    }
}

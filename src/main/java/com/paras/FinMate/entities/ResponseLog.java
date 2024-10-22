package com.paras.FinMate.entities;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;

import java.sql.Timestamp;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "response_log")
public class ResponseLog {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "query_id", nullable = false)
    private EmailQueryTracking query;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String responseBody;

    @Column(nullable = false)
    private String threadId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String requestBody;

    @Column(nullable = false)
    private Timestamp sentAt;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ResponseType responseType;

    @PrePersist
    private void prePersist () {
        if (this.id == null) {
            this.id = generateResponseLogId();
        }
        this.sentAt = new Timestamp(System.currentTimeMillis());
    }

    private String generateResponseLogId () {
        return "RESP" + RandomStringUtils.randomNumeric(6);
    }

    public enum ResponseType {
        AUTO,
        MANUAL
    }
}

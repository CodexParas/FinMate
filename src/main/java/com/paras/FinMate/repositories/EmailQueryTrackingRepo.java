package com.paras.FinMate.repositories;

import com.paras.FinMate.entities.EmailQueryTracking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailQueryTrackingRepo extends JpaRepository<EmailQueryTracking, String> {
    Optional<EmailQueryTracking> findByThreadId (String threadId);

}

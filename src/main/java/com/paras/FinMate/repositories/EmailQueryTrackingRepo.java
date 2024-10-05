package com.paras.FinMate.repositories;

import com.paras.FinMate.entities.EmailQueryTracking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailQueryTrackingRepo extends JpaRepository<EmailQueryTracking, String> {
}

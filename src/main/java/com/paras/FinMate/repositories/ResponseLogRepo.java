package com.paras.FinMate.repositories;

import com.paras.FinMate.entities.ResponseLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResponseLogRepo extends JpaRepository<ResponseLog, String> {
    Optional<List<ResponseLog>> findAllByThreadId (String threadId);
}

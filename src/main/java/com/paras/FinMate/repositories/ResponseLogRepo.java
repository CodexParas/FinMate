package com.paras.FinMate.repositories;

import com.paras.FinMate.entities.ResponseLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResponseLogRepo extends JpaRepository<ResponseLog, String> {
}

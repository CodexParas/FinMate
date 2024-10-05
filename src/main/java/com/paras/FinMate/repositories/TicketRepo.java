package com.paras.FinMate.repositories;

import com.paras.FinMate.entities.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepo extends JpaRepository<Ticket, String> {
}

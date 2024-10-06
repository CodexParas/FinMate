package com.paras.FinMate.controllers;

import com.paras.FinMate.common.Response;
import com.paras.FinMate.entities.EmailQueryTracking;
import com.paras.FinMate.entities.ResponseLog;
import com.paras.FinMate.entities.Ticket;
import com.paras.FinMate.exceptions.DataNotFoundException;
import com.paras.FinMate.repositories.EmailQueryTrackingRepo;
import com.paras.FinMate.repositories.ResponseLogRepo;
import com.paras.FinMate.repositories.TicketRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/get", produces = "application/json")
public class GetDataController {

    private final EmailQueryTrackingRepo emailQueryTrackingRepo;
    private final ResponseLogRepo responseLogRepo;
    private final TicketRepo ticketRepo;

    @GetMapping("/emailQueryTracking")
    public ResponseEntity<Response> getEmailQueryTracking () {
        List<EmailQueryTracking> emailQueryTrackingList = emailQueryTrackingRepo.findAll();
        return ResponseEntity.ok(Response.success("Data found.", emailQueryTrackingList));
    }

    @GetMapping("/emailQueryTracking/{id}")
    public ResponseEntity<Response> getEmailQueryTrackingById (@PathVariable String id) {
        EmailQueryTracking emailQueryTracking = emailQueryTrackingRepo.findById(id).orElseThrow(() -> new DataNotFoundException("Email Query Tracking not found"));
        return ResponseEntity.ok(Response.success("Data found.", emailQueryTracking));
    }

    @GetMapping("/responseLogs")
    public ResponseEntity<Response> getResponseLogs () {
        List<ResponseLog> responseLogList = responseLogRepo.findAll();
        return ResponseEntity.ok(Response.success("Data found.", responseLogList));
    }

    @GetMapping("/responseLogs/{id}")
    public ResponseEntity<Response> getResponseLogsById (@PathVariable String id) {
        ResponseLog responseLog = responseLogRepo.findById(id).orElseThrow(() -> new DataNotFoundException("Response Logs not found"));
        return ResponseEntity.ok(Response.success("Data found.", responseLog));
    }

    @GetMapping("/responseLogs/thread/{id}")
    public ResponseEntity<Response> getResponseLogsByThreadId (@PathVariable String id) {
        List<ResponseLog> responseLogList = responseLogRepo.findAllByThreadId(id).orElseThrow(() -> new DataNotFoundException("Response Logs not found"));
        return ResponseEntity.ok(Response.success("Data found.", responseLogList));
    }

    @GetMapping("/tickets")
    public ResponseEntity<Response> getTickets () {
        List<Ticket> ticketList = ticketRepo.findAll();
        return ResponseEntity.ok(Response.success("Data found.", ticketList));
    }

    @GetMapping("/tickets/{id}")
    public ResponseEntity<Response> getTickets (@PathVariable String id) {
        Ticket ticket = ticketRepo.findById(id).orElseThrow(() -> new DataNotFoundException("Ticket not found"));
        return ResponseEntity.ok(Response.success("Data found.", ticket));
    }


}

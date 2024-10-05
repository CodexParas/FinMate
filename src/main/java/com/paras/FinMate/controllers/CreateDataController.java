package com.paras.FinMate.controllers;

import com.paras.FinMate.DTO.CreateCustomerRequest;
import com.paras.FinMate.DTO.TransactionRequest;
import com.paras.FinMate.common.Response;
import com.paras.FinMate.services.CustomerService;
import com.paras.FinMate.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/create")
public class CreateDataController {

    private final CustomerService customerService;
    private final TransactionService transactionService;

    @PostMapping("/customer")
    public ResponseEntity<Response> createCustomer (@RequestBody @Valid CreateCustomerRequest createCustomerRequest) {
        // Customer creation logic
        return ResponseEntity.ok(customerService.createCustomer(createCustomerRequest));
    }

    @PostMapping("/transaction")
    public ResponseEntity<Response> transaction (@RequestBody @Valid TransactionRequest transactionRequest) {
        // Transaction logic
        return ResponseEntity.ok(transactionService.transaction(transactionRequest));
    }

}

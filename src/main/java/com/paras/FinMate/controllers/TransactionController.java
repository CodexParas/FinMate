package com.paras.FinMate.controllers;

import com.paras.FinMate.common.Response;
import com.paras.FinMate.requests.TransactionRequest;
import com.paras.FinMate.services.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transaction")
    public ResponseEntity<Response> transaction (@RequestBody @Valid TransactionRequest transactionRequest) {
        // Transaction logic
        return ResponseEntity.ok(transactionService.transaction(transactionRequest));
    }
}

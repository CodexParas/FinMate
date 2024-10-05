package com.paras.FinMate.services;

import com.paras.FinMate.common.Response;
import com.paras.FinMate.entities.Customer;
import com.paras.FinMate.entities.Transaction;
import com.paras.FinMate.exceptions.CustomerNotFoundException;
import com.paras.FinMate.repositories.CustomerRepo;
import com.paras.FinMate.repositories.TransactionRepo;
import com.paras.FinMate.requests.TransactionRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.paras.FinMate.entities.Transaction.getCurrency;
import static com.paras.FinMate.entities.Transaction.getTransactionType;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final CustomerRepo customerRepo;
    private final TransactionRepo transactionRepo;

    public Response transaction (@Valid TransactionRequest transactionRequest) {
        Customer customer = customerRepo.findByEmail(transactionRequest.getCustomerId()).orElseThrow(() -> new CustomerNotFoundException("Customer not found"));
        // Transaction logic
        Transaction transaction = Transaction.builder()
                                             .transactionType(getTransactionType(transactionRequest.getTransactionType()))
                                             .amount(Double.parseDouble(transactionRequest.getTransactionAmount()))
                                             .currency(getCurrency(transactionRequest.getTransactionCurrency()))
                                             .customer(customer)
                                             .swiftCode(transactionRequest.getSwiftCode().isBlank() || transactionRequest.getSwiftCode().isEmpty() ? null : transactionRequest.getSwiftCode())
                                             .build();
        // Save transaction
        transactionRepo.save(transaction);
        return Response.success("Transaction successful", transaction);
    }
}

package com.paras.FinMate.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TransactionRequest {
    @NotEmpty(message = "Customer ID is required")
    @NotBlank(message = "Customer ID is required")
    private String customerId;
    @NotEmpty(message = "Transaction Type is required")
    @NotBlank(message = "Transaction Type is required")
    private String transactionType;
    @NotEmpty(message = "Transaction Amount is required")
    @NotBlank(message = "Transaction Amount is required")
    private String transactionAmount;
    @NotEmpty(message = "Transaction Currency is required")
    @NotBlank(message = "Transaction Currency is required")
    private String transactionCurrency;
    private String swiftCode;
}

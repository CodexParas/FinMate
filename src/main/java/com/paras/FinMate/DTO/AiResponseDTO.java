package com.paras.FinMate.DTO;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AiResponseDTO {
    private String senderEmail;
    private String transactionId;
    private String transactionAmount;
    private String transactionCurrency;
    private String transactionDate;
    private String transactionType;
    private String transactionQueryType;

}

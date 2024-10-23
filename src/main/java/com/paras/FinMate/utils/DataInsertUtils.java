package com.paras.FinMate.utils;

import com.paras.FinMate.DTO.AiResponseDTO;
import com.paras.FinMate.DTO.GmailDTO;
import com.paras.FinMate.entities.*;
import com.paras.FinMate.repositories.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import static com.paras.FinMate.entities.EmailQueryTracking.ResolutionStatus.PENDING;
import static com.paras.FinMate.entities.ResponseLog.ResponseType.AUTO;
import static com.paras.FinMate.entities.Ticket.TicketStatus.OPEN;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInsertUtils {

    private final CustomerRepo customerRepo;
    private final EmailQueryTrackingRepo emailQueryTrackingRepo;
    private final ResponseLogRepo responseLogRepo;
    private final TicketRepo ticketRepo;
    private final TransactionRepo transactionRepo;

    public void insertEmailQuery (GmailDTO gmailDTO, AiResponseDTO aiResponseDTO) {
        Customer customer = customerRepo.findByEmail(gmailDTO.getFrom())
                                        .orElse(null);
        Transaction transaction = transactionRepo.findById(aiResponseDTO.getTransactionId())
                                                 .orElse(null);
        EmailQueryTracking emailQueryTracking = EmailQueryTracking.builder()
                                                                  .emailBody(gmailDTO.getBody())
                                                                  .customer(customer)
                                                                  .transaction(transaction)
                                                                  .queryType(aiResponseDTO.getTransactionQueryType())
                                                                  .emailSubject(gmailDTO.getSubject())
                                                                  .attemptCount(1)
                                                                  .threadId(gmailDTO.getThreadId())
                                                                  .resolutionStatus(PENDING)
                                                                  .build();
        emailQueryTrackingRepo.save(emailQueryTracking);
    }

    public void insertResponseLog (GmailDTO gmailDTO, String response) {
        ResponseLog responseLog = ResponseLog.builder()
                                             .responseType(AUTO)
                                             .requestBody(gmailDTO.getBody())
                                             .threadId(gmailDTO.getThreadId())
                                             .responseBody(Jsoup.parse(response).text())
                                             .query(emailQueryTrackingRepo.findByThreadId(gmailDTO.getThreadId()).orElse(null))
                                             .build();
        responseLogRepo.save(responseLog);
    }

    public String insertTicket (GmailDTO gmailDTO) {
        String email = extractEmail(gmailDTO.getFrom());
        Ticket ticket = Ticket.builder()
                              .customer(customerRepo.findByEmail(email).orElse(null))
                              .ticketStatus(OPEN)
                              .assignedAgent("Paras")
                              .query(emailQueryTrackingRepo.findByThreadId(gmailDTO.getThreadId()).orElse(null))
                              .build();
        ticketRepo.save(ticket);
        return ticket.getId();
    }

    public String extractEmail (String input) {
        if (input == null || input.isEmpty()) {
            return null;
        }
        if (input.contains("<") && input.contains(">")) {
            return input.substring(input.indexOf("<") + 1, input.indexOf(">")).trim();
        }
        return input.trim();
    }
}

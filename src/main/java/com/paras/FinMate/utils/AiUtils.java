package com.paras.FinMate.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paras.FinMate.DTO.AiResponseDTO;
import com.paras.FinMate.entities.Customer;
import com.paras.FinMate.entities.Transaction;
import com.paras.FinMate.repositories.CustomerRepo;
import com.paras.FinMate.repositories.TransactionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AiUtils {

    private final OpenAiChatModel client;
    private final ObjectMapper objectMapper;
    private final CustomerRepo customerRepo;
    private final TransactionRepo transactionRepo;

    public AiResponseDTO getResponse (JsonNode node) throws JsonProcessingException {
        String promptText = String.format("""
                                                  Hi, You are my personal Email Assistant. I have an email mentioned below.
                                                  I want you to process this email and get me some suitable details in a well-structured format.
                                                  First, you need to extract the email of the sender. Then his transaction id which must be in the format FINMXXXXXX where X represents a number.
                                                  Then you need to extract the amount in Double, date, and type (domestic or international) of the transaction also the type of Query(STATUS or REFUND).
                                                  You can use the following JSON format to extract the details. If any of the details are not present, you can put NA:
                                                     - senderEmail
                                                     - transactionId
                                                     - transactionAmount
                                                     - transactionCurrency
                                                     - transactionDate
                                                     - transactionType
                                                     - transactionQueryType
                                                  Note:- Just return me JSON without any other text or symbols also without json text .
                                                  %s
                                                  """, node.toString());

        Prompt prompt = new Prompt(promptText);
        ChatResponse chatResponse = client.call(prompt);

        String res = chatResponse.getResult().getOutput().getContent();
        return objectMapper.readValue(res, AiResponseDTO.class);
    }

    public String getReplyBody (AiResponseDTO aiResponseDTO) {
        Customer customer = customerRepo.findByEmail(aiResponseDTO.getSenderEmail()).orElse(null);
        if (customer == null) {
            // HTML mail for your mail id is not registered with us please mail from your registered mail id
            return """
                    <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #555;">
                        <h2 style="color: #333;">Registration Error</h2>
                        <p>Your mail ID is not registered with us. Please mail from your registered mail ID.</p>
                        <p>Thank you for your understanding.</p>
                    </div>
                    """;

        }
        if (aiResponseDTO.getTransactionId().equals("NA")) {
            Pageable pageable = Pageable.ofSize(3);
            List<Transaction> transactions = transactionRepo.findAllByCustomer_Id(customer.getId(), pageable).orElse(null);
            if (transactions == null || transactions.isEmpty()) {
                // No transactions found for your account
                return """
                        <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #555;">
                            <h2 style="color: #333;">No Transactions Found</h2>
                            <p>No transactions found for your account.</p>
                            <p>Thank you for your understanding.</p>
                        </div>
                        """;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("""
                              <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #555;">
                                  <h2 style="color: #333;">Transactions</h2>
                                  <table style="width: 100%; border-collapse: collapse;">
                                      <thead>
                                          <tr>
                                              <th style="border: 1px solid #ddd; padding: 8px;">Transaction ID</th>
                                              <th style="border: 1px solid #ddd; padding: 8px;">Amount</th>
                                              <th style="border: 1px solid #ddd; padding: 8px;">Date</th>
                                              <th style="border: 1px solid #ddd; padding: 8px;">Type</th>
                                          </tr>
                                      </thead>
                                      <tbody>
                              """);
            for (Transaction transaction : transactions) {
                sb.append("""
                                  <tr>
                                      <td style="border: 1px solid #ddd; padding: 8px;">""").append(transaction.getId()).append("</td>")
                  .append("<td style=\"border: 1px solid #ddd; padding: 8px;\">").append(transaction.getAmount()).append("</td>")
                  .append("<td style=\"border: 1px solid #ddd; padding: 8px;\">").append(transaction.getTransactionDate()).append("</td>")
                  .append("<td style=\"border: 1px solid #ddd; padding: 8px;\">").append(transaction.getTransactionType()).append("</td>")
                  .append("</tr>");
            }
            sb.append("""
                                  </tbody>
                              </table>""");
            //ask to reply with any transaction id
            sb.append("""
                                  <p>Please reply with a transaction ID to get more details.</p>
                                  <p>Thank you for your understanding.</p>
                              </div>
                              """);
            return sb.toString();
        }
        Transaction transaction = transactionRepo.findById(aiResponseDTO.getTransactionId()).orElse(null);
        if (transaction == null) {
            // Transaction not found
            return """
                    <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #555;">
                        <h2 style="color: #333;">Transaction Not Found</h2>
                        <p>Transaction not found.</p>
                        <p>Thank you for your understanding.</p>
                    </div>
                    """;
        }
        // Transaction found
        if (aiResponseDTO.getTransactionQueryType().equalsIgnoreCase("STATUS") || aiResponseDTO.getTransactionQueryType().equalsIgnoreCase("NA")) {
            return """
                    <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #555;">
                        <h2 style="color: #333;">Transaction Details</h2>
                        <table style="width: 100%; border-collapse: collapse;">
                            <thead>
                                <tr>
                                    <th style="border: 1px solid #ddd; padding: 8px;">Transaction ID</th>
                                    <th style="border: 1px solid #ddd; padding: 8px;">Amount</th>
                                    <th style="border: 1px solid #ddd; padding: 8px;">Date</th>
                                    <th style="border: 1px solid #ddd; padding: 8px;">Type</th>
                                    <th style="border: 1px solid #ddd; padding: 8px;">Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td style="border: 1px solid #ddd; padding: 8px;">""" +
                    transaction.getId() + "</td>" +
                    "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + transaction.getAmount() + "</td>" +
                    "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + transaction.getTransactionDate() + "</td>" +
                    "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + transaction.getTransactionType() + "</td>" +
                    "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + transaction.getStatus() + "</td>" +
                    "</tr>" +
                    """
                                    </tbody>
                                </table>
                                <p>Thank you for your understanding.</p>
                            </div>
                            """;
        } else {
            return """
                    <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #555;">
                        <h2 style="color: #333;">Transaction Refund</h2>
                        <table style="width: 100%; border-collapse: collapse;">
                            <thead>
                                <tr>
                                    <th style="border: 1px solid #ddd; padding: 8px;">Transaction ID</th>
                                    <th style="border: 1px solid #ddd; padding: 8px;">Amount</th>
                                    <th style="border: 1px solid #ddd; padding: 8px;">Date</th>
                                    <th style="border: 1px solid #ddd; padding: 8px;">Type</th>
                                    <th style="border: 1px solid #ddd; padding: 8px;">Refund Status</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr>
                                    <td style="border: 1px solid #ddd; padding: 8px;">""" +
                    transaction.getId() + "</td>" +
                    "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + transaction.getAmount() + "</td>" +
                    "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + transaction.getTransactionDate() + "</td>" +
                    "<td style=\"border: 1px solid #ddd; padding: 8px;\">" + transaction.getTransactionType() + "</td>" +
                    "<td style=\"border: 1px solid #ddd; padding: 8px;\">Refunded</td>" +
                    "</tr>" +
                    """
                                    </tbody>
                                </table>
                                <p>Thank you for your understanding.</p>
                            </div>
                            """;
        }
    }

    public String getAttemptsReachedReply (String ticketId) {
        return String.format(
                "Dear Customer, <br><br>" +
                        "Thank you for reaching out to us. Our executives will contact you shortly regarding your request. " +
                        "Please note your ticket ID for reference: %s. <br><br>" +
                        "Thank you for your patience.<br><br>" +
                        "Best regards,<br>" +
                        "Customer Support Team",
                ticketId
                            );
    }
}
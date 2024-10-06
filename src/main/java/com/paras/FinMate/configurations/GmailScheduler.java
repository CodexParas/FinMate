package com.paras.FinMate.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paras.FinMate.DTO.AiResponseDTO;
import com.paras.FinMate.DTO.GmailDTO;
import com.paras.FinMate.entities.EmailQueryTracking;
import com.paras.FinMate.repositories.EmailQueryTrackingRepo;
import com.paras.FinMate.utils.AiUtils;
import com.paras.FinMate.utils.DataInsertUtils;
import com.paras.FinMate.utils.GmailUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class GmailScheduler {
    private final AiUtils aiUtils;
    private final ObjectMapper objectMapper;
    private final GmailUtils gmailUtils;
    private final EmailQueryTrackingRepo emailQueryTrackingRepo;
    private final DataInsertUtils dataInsertUtils;

    @SneakyThrows
    @Scheduled(fixedRate = 1000 * 60 * 60)
    public void sendEmails () {
        log.info("Checking for new emails");
        List<GmailDTO> gmailDTOList = gmailUtils.getGmails().orElse(List.of());
        if (gmailDTOList.isEmpty()) {
            log.info("No new emails");
            return;
        }
        log.info("Found {} mails.", gmailDTOList.size());
        for (GmailDTO gmailDTO : gmailDTOList) {
            log.info("Processing mail: {}", gmailDTO);
            EmailQueryTracking emailQueryTracking = emailQueryTrackingRepo.findByThreadId(gmailDTO.getThreadId()).orElse(null);
            if (emailQueryTracking != null && emailQueryTracking.getAttemptCount() >= 3) {
                log.info("Email query already exists and has been attempted 3 times, assigning manual ticket.");
                String ticketId = dataInsertUtils.insertTicket(gmailDTO);
                log.info("Ticket ID: {}", ticketId);
                String response = aiUtils.getAttemptsReachedReply(ticketId);
                gmailUtils.sendReply(gmailDTO, response);
                dataInsertUtils.insertResponseLog(gmailDTO, response);
                gmailUtils.markAsRead(gmailDTO.getMessage());
                continue;
            }
            AiResponseDTO aiResponseDTO = aiUtils.getResponse(objectMapper.valueToTree(gmailDTO));
            if (emailQueryTracking == null) {
                log.info("Inserting email query");
                dataInsertUtils.insertEmailQuery(gmailDTO, aiResponseDTO);
            } else {
                log.info("Email query already exists");
                emailQueryTracking.setAttemptCount(emailQueryTracking.getAttemptCount() + 1);
                emailQueryTrackingRepo.save(emailQueryTracking);
            }
            log.info("Response: {}", aiResponseDTO);
            String response = aiUtils.getReplyBody(aiResponseDTO);
            log.info("Reply: {}", response);
            gmailUtils.sendReply(gmailDTO, response);
            dataInsertUtils.insertResponseLog(gmailDTO, response);
            gmailUtils.markAsRead(gmailDTO.getMessage());
        }
    }
}

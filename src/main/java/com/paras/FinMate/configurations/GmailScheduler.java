package com.paras.FinMate.configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paras.FinMate.DTO.AiResponseDTO;
import com.paras.FinMate.DTO.GmailDTO;
import com.paras.FinMate.utils.AiUtils;
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
            AiResponseDTO aiResponseDTO = aiUtils.getResponse(objectMapper.valueToTree(gmailDTO));
            log.info("Response: {}", aiResponseDTO);
            String response = aiUtils.getReplyBody(aiResponseDTO);
            log.info("Reply: {}", response);
            gmailUtils.sendReply(gmailDTO, response);
            gmailUtils.markAsRead(gmailDTO.getMessage());
        }
    }
}

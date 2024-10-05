package com.paras.FinMate.utils;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Thread;
import com.google.api.services.gmail.model.*;
import com.paras.FinMate.DTO.GmailDTO;
import com.paras.FinMate.services.CredentialService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.jsoup.Jsoup;
import org.springframework.stereotype.Component;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Component
@RequiredArgsConstructor
@Slf4j
public class GmailUtils {
    private static final String APPLICATION_NAME = "Your Spring Boot App";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private final CredentialService credentialService;
    private final String user = "me";

    @SneakyThrows
    private Gmail getGmailService () {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Credential credential = credentialService.refreshCredential();
        if (credential == null) {
            return null;
        }
        return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @SneakyThrows
    public Optional<List<GmailDTO>> getGmails () {
        Gmail service = getGmailService();
        if (service == null) {
            return Optional.empty();
        }
        ListMessagesResponse mailList = service.users().messages().list(user).setQ("is:unread").execute();
        List<Message> messages = mailList.getMessages();
        if (messages == null || messages.isEmpty()) {
            log.info("No new messages");
            return Optional.empty();
        }
        List<GmailDTO> mailDTOList = new java.util.ArrayList<>(List.of());
        for (Message message : messages) {
            Message messageDetails = service.users().messages().get(user, message.getId()).execute();
            GmailDTO mailDTO = getConvertedMail(messageDetails);
            log.info("Body: {}", mailDTO);
            mailDTOList.add(mailDTO);
        }
        return Optional.of(mailDTOList);
    }

    private GmailDTO getConvertedMail (Message message) {
        GmailDTO mailDTO = new GmailDTO();
        List<MessagePartHeader> headers = message.getPayload().getHeaders();
        for (MessagePartHeader header : headers) {
            switch (header.getName()) {
                case "From":
                    mailDTO.setFrom(header.getValue());
                    break;
                case "Subject":
                    mailDTO.setSubject(header.getValue());
                    break;
                case "To":
                    mailDTO.setTo(header.getValue());
                    break;
            }
            mailDTO.setThreadId(message.getThreadId());
        }
        mailDTO.setBody(Jsoup.parse(getMessageBody(message)).text());
        mailDTO.setMessage(message);
        return mailDTO;
    }

    private String getMessageBody (Message message) {
        if (message.getPayload().getMimeType().equals("text/plain")) {
            return new String(Base64.decodeBase64(message.getPayload().getBody().getData()));
        } else if (message.getPayload().getMimeType().equals("text/html")) {
            return new String(Base64.decodeBase64(message.getPayload().getBody().getData()));
        } else {
            return getMultipartMessageBody(message.getPayload().getParts());
        }
    }


    private String getHeader (Message message, String headerName) {
        for (MessagePartHeader header : message.getPayload().getHeaders()) {
            if (header.getName().equalsIgnoreCase(headerName)) {
                return header.getValue();
            }
        }
        return null;
    }

    private String getMultipartMessageBody (List<MessagePart> parts) {
        for (MessagePart part : parts) {
            if (part.getMimeType().equals("text/plain")) {
                return new String(Base64.decodeBase64(part.getBody().getData()));
            } else if (part.getMimeType().equals("text/html")) {
                return new String(Base64.decodeBase64(part.getBody().getData()));
            } else if (part.getParts() != null && !part.getParts().isEmpty()) {
                // Recursively handle nested multipart messages
                return getMultipartMessageBody(part.getParts());
            }
        }
        return "";
    }

    @SneakyThrows
    public void sendReply (GmailDTO gmailDTO, String replyText) {
        Gmail service = getGmailService();
        if (service == null) {
            return;
        }
        String threadId = gmailDTO.getThreadId();

        MimeMessage reply = createEmailReply(gmailDTO, replyText);
        Message replyMessage = createMessageWithEmail(reply);
        replyMessage.setThreadId(threadId);
        service.users().messages().send(user, replyMessage).execute();
        log.info("Reply sent and threaded successfully!");
    }

    @SneakyThrows
    private MimeMessage createEmailReply (GmailDTO gmailDTO, String replyText) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage reply = new MimeMessage(session);
        String originalMessageIdHeader = getHeader(gmailDTO.getMessage(), "Message-ID");
        reply.addRecipient(javax.mail.Message.RecipientType.TO, new InternetAddress(gmailDTO.getFrom()));
        if (gmailDTO.getSubject().contains("Re: ")) {
            reply.setSubject(gmailDTO.getSubject());
        } else {
            reply.setSubject("Re: " + gmailDTO.getSubject());
        }
        reply.setContent(replyText, "text/html; charset=utf-8");
        reply.setHeader("In-Reply-To", originalMessageIdHeader);
        reply.setHeader("References", originalMessageIdHeader);
        return reply;
    }

    @SneakyThrows
    private Message createMessageWithEmail (MimeMessage email) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        email.writeTo(buffer);
        byte[] rawData = buffer.toByteArray();
        String encodedEmail = Base64.encodeBase64URLSafeString(rawData);
        Message message = new Message();
        message.setRaw(encodedEmail);
        return message;
    }

    @SneakyThrows
    public void markAsRead (Message message) {
        Gmail service = getGmailService();
        if (service == null) {
            return;
        }
        String threadId = message.getThreadId();
        Thread thread = service.users().threads().get(user, threadId).execute();
        List<Message> messagesInThread = thread.getMessages();
        for (Message msg : messagesInThread) {
            service.users().messages().modify(
                    user,
                    msg.getId(),
                    new ModifyMessageRequest().setRemoveLabelIds(Collections.singletonList("UNREAD"))
                                             ).execute();
        }
    }

}

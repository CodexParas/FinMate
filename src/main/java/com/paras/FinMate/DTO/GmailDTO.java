package com.paras.FinMate.DTO;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.api.services.gmail.model.Message;
import lombok.*;

@Getter
@Setter
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GmailDTO {
    private String from;
    private String to;
    private String subject;
    private String body;
    private String threadId;
    @JsonIgnore
    private Message message;
}

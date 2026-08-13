package com.isthisalis.ailib.util.DTO.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.isthisalis.ailib.util.DTO.Message;

import lombok.Data;

/**
 * AiResponse
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiResponse {
    private List<Choice> choices;

    @Data
    public class Choice {
        private Message message;
    }
}

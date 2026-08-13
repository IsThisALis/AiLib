package com.isthisalis.ailib.util.DTO.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.isthisalis.ailib.util.DTO.Message;

import lombok.Builder;
import lombok.Data;

/**
 * AiRequest
 */
@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AiRequest {
    private String model;
    private List<Message> messages;
    private List<Tool> tools;
    private @Builder.Default Boolean stream = false;
    private @JsonUnwrapped RequestParams params;
}

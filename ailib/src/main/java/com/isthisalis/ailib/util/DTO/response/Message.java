package com.isthisalis.ailib.util.DTO.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.isthisalis.ailib.util.DTO.ToolCall;

/**
 * Message
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public record Message(String role, String content, String refusal, @JsonProperty("tool_calls") List<ToolCall> toolCalls) {

  public Message {
    if (role == null) { role = ""; }
    if (content == null) { content = ""; }
  }
}

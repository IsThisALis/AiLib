package com.isthisalis.ailib.util.DTO.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.isthisalis.ailib.util.DTO.ToolCall;

/**
 * Message
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Message(String role, String content, @JsonProperty("tool_calls") List<ToolCall> toolCalls, @JsonProperty("tool_call_id") String toolCallId, String name) {
  public Message(String role, String content) {
    this(role, content, null, null, null);
  }


  public Message(String role, String content, List<ToolCall> toolCalls) {
    this(role, content, toolCalls, null, null);
  }


  public Message(String toolCallId, String name, String content) {
    this("tool", content, null, toolCallId, name);
  }

  public Message {
    if (content == null) content = "";
    if (role == null) role = "";
  }
}

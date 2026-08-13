package com.isthisalis.ailib.util.DTO;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Message
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public record Message(String role,
  String content, 
  @JsonProperty("tool_calls") List<ToolCall> toolCalls, 
  @JsonProperty("tool_call_id") String toolCallId, 
  String name) {

  public Message {
    if (content == null) content = "";
    if (role == null) role = "";
  }

  public static Message system(String content) {
        return new Message("system", content, null, null, null);
    }

    public static Message user(String content) {
        return new Message("user", content, null, null, null);
    }

    public static Message assistant(String content) {
        return new Message("assistant", content, null, null, null);
    }

    public static Message assistant(String content, List<ToolCall> toolCalls) {
        return new Message("assistant", content, toolCalls, null, null);
    }

    public static Message tool(String toolCallId, String name, String content) {
        return new Message("tool", content, null, toolCallId, name);
    }
    
}

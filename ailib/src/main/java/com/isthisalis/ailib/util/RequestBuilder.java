package com.isthisalis.ailib.util;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.type.TypeReference;

import com.isthisalis.ailib.util.DTO.request.AiRequest;
import com.isthisalis.ailib.util.DTO.request.RequestParams;
import com.isthisalis.ailib.util.DTO.request.Tool;
import com.isthisalis.ailib.util.DTO.Message;
import com.isthisalis.ailib.util.DTO.ToolCall;
import com.isthisalis.ailib.util.DTO.response.AiResponse;
import com.isthisalis.ailib.util.DTO.response.AiResponse.Choice;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import com.isthisalis.ailib.api.Configuration;
import com.isthisalis.ailib.api.ai.AiService;
import com.isthisalis.ailib.api.ai.tools.ToolCallParser;

import com.isthisalis.ailib.exception.*;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Builds requests to AI and parses responses.
 */
public class RequestBuilder {

  private static Logger logger = Logger.getGlobal();
  private ObjectMapper mapper = new ObjectMapper();
  private AiService ai;
  private ToolCallParser toolCallParser;

  private String model;
  private String systemPrompt;
  private List<Tool> tools = new ArrayList<>();
  private @Getter @Setter RequestParams requestParams;

  public RequestBuilder(@NonNull Configuration config, AiService ai, ToolCallParser toolParser) {
    this.model = config.getModel();
    this.systemPrompt = config.getBio() + "\n\n" + config.getRules();
    this.ai = ai;
    this.toolCallParser = toolParser;
  }

  public AiRequest makeAiRequest(String message) throws Exception {
    var settings = Message.system(systemPrompt);
    var userMsg = Message.user(message);
    var request = AiRequest.builder()
    .model(model)
    .messages(List.of(settings, userMsg))
    .tools(tools)
    .build();

    if (requestParams != null) { request.setParams(requestParams); }

    return request;
  }


  public AiRequest makeAiRequest(List<Message> history) throws Exception {
    var request = AiRequest.builder()
    .model(model)
    .messages(history)
    .tools(tools)
    .build();

    if (requestParams != null) { request.setParams(requestParams); }

    return request;
  }

  public String parseAiResponse(List<Message> currHistory, String json) throws ApiException, HttpIOException {
    String newResp = null;
    AiResponse response = mapper.readValue(json, AiResponse.class);
    Choice choice = response.getChoices().get(0);
    Message msg = choice.getMessage();

    if (msg.content() != null && !msg.content().isBlank()) { logger.info(msg.content()); return msg.content(); }

    if (msg.toolCalls() != null && !msg.toolCalls().isEmpty()) {
      logger.info("Tool Called by: " + model);
      currHistory.add(Message.assistant(msg.content(), msg.toolCalls()));

      for (ToolCall toolCall : msg.toolCalls()) {
        currHistory.add(toolCallParser.parseToolCalls(toolCall));
      }

      AiRequest newReq = AiRequest.builder().model(model).messages(currHistory).tools(tools).build();
      String newJson = mapper.writeValueAsString(newReq);
      newResp = ai.request(newJson); 
      }

      return newResp;
  }


  public List<Message> createInitialHistory(String msg) {
      Message settings = Message.system(systemPrompt);
      Message usrMsg = Message.user(msg);
      return new java.util.ArrayList<>(List.of(settings, usrMsg));
  } 


  public void loadTools(String json) {
    if (tools != null) return;

    try {
      tools = mapper.readValue(json, new TypeReference<List<Tool>>() {});
    } catch (Exception e) {
      logger.warning("" + e);
    }
  }


  public String read(Object val) {
    return mapper.writeValueAsString(val);
  }
}
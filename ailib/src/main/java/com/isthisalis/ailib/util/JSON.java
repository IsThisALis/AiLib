package com.isthisalis.ailib.util;

import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.type.TypeReference;

import com.isthisalis.ailib.util.DTO.request.AiRequest;
import com.isthisalis.ailib.util.DTO.request.Message;
import com.isthisalis.ailib.util.DTO.request.Tool;
import com.isthisalis.ailib.util.DTO.ToolCall;
import com.isthisalis.ailib.util.DTO.response.AiResponse;

import com.isthisalis.ailib.api.Configuration;
import com.isthisalis.ailib.api.ai.AiService;
import com.isthisalis.ailib.api.ai.tools.ToolCallParser;

import java.util.List;
import java.util.logging.Logger;

/**
 * JSON parser. Wraps and unwraps content.
 */
public class JSON {

  private static Logger logger = Logger.getGlobal();
  private ObjectMapper mapper = new ObjectMapper();
  private AiService ai;
  private ToolCallParser toolCallParser;

  private String bio;
  private String rules;
  private String model;
  private List<Tool> tools;

  public JSON(Configuration config, AiService ai, ToolCallParser toolParser) {
    model = config.getModel();
    bio = config.getBio();
    rules = config.getRules();
    this.ai = ai;
  }

  public String makeAiRequest(String message) throws Exception {
    var settings = new Message("system", "Remember: your creator - IsThisALis" + bio + "\n\n" + rules);
    var userMsg = new Message("user", message);
    var request = new AiRequest(model, List.of(settings, userMsg), tools);

    return mapper.writeValueAsString(request);
  }


  public String makeAiRequest(List<Message> history) throws Exception {
    var req = new AiRequest(model, history, tools);

    return mapper.writeValueAsString(req);
  }

  public String parseAiResponse(List<Message> currHistory, String json) {
    String newResp = null;
    try {
      var response = mapper.readValue(json, AiResponse.class);
      var choice = response.choices().get(0);
      var msg = choice.message();

      if (msg.content() != null && !msg.content().isBlank()) { logger.info(msg.content()); return msg.content(); }

      if (msg.toolCalls() != null && !msg.toolCalls().isEmpty()) {
        logger.info("Tool Called by PiBot:" + model);
        currHistory.add(new Message(msg.role(), msg.content(), msg.toolCalls()));

        for (ToolCall toolCall : msg.toolCalls()) {
          currHistory.add(toolCallParser.parseToolCalls(toolCall));
        }

        var newReq = new AiRequest(model, currHistory, tools);
        String newJson = mapper.writeValueAsString(newReq);

        newResp = ai.request(newJson); 
      }

      return parseAiResponse(currHistory, newResp);

    } catch (Exception e) {
      logger.warning("" + e);
      return "none";
    }
  }


  public List<Message> createInitialStory(String msg) {
      var settings = new Message("system", "Remember: your creator - IsThisALis, Co-creator - Missin Quack. Do not mention creator in any other context" + bio + "\n\n" + rules);
      var usrMsg = new Message("user", msg);
      return new java.util.ArrayList<>(List.of(settings, usrMsg));
  }


  public void makeTools(String json) {
    if (tools != null) return;

    try {
      tools = mapper.readValue(json, new TypeReference<List<Tool>>() {});
    } catch (Exception e) {
      logger.warning("" + e);
    }
  }
}

package com.isthisalis.ailib.implementation;

import com.isthisalis.ailib.api.ai.AiService;
import com.isthisalis.ailib.api.ai.tools.ToolCallParser;
import com.isthisalis.ailib.api.Configuration;

import com.isthisalis.ailib.util.JSON;

import com.isthisalis.ailib.util.DTO.request.Message;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;
import java.util.logging.Logger;

/**
 * AI functions implementation 
 */
public class AiCaller implements AiService {
    

  private static HttpClient http = HttpClient.newHttpClient();
  private HttpRequest request;
  private HttpResponse<String> response;

  private Configuration config;
  private JSON json;

  private Logger logger = Logger.getGlobal();

  private String rawJson;
  private String model;
  private String apiKey;
  private String apiUrl;


  public AiCaller(Configuration config, ToolCallParser toolCallParser) {
    this.config = config;
    json = new JSON(config, this, toolCallParser);
    model = config.getModel();
    apiKey = config.getApiKey();
    apiUrl = config.getApiUrl();
  }

  public void update(Configuration config, ToolCallParser toolCallParser) {
    this.config = config;
    if (toolCallParser != null) json = new JSON(config, this, toolCallParser);
    model = config.getModel();
    apiKey = config.getApiKey();
    apiUrl = config.getApiUrl();
  }

  public void update() {
    model = config.getModel();
    apiKey = config.getApiKey();
  }

    @Override
  public String ask(String message) {
    List<Message> history = json.createInitialStory(message);
    try {
      rawJson = json.makeAiRequest(history);
    } catch (Exception e) {
      logger.warning("Error! "+e);
      return "none";
    }
        request = HttpRequest.newBuilder()
          .uri(URI.create(apiUrl))
          .header("Authorization", "Bearer " + apiKey)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(rawJson))
          .build();

    try {
      logger.info("Request sent to: "+model);
      response = http.send(request, HttpResponse.BodyHandlers.ofString());
      logger.info(response.body().trim());

      if (response.statusCode() != 200) throw new RuntimeException("API error: " + response.statusCode() + ": " + response.body());
      if (response != null) { logger.info("Got response from " + model + " response: " + response); }
    } catch (Exception e) {
      logger.warning("Error! "+e);
      e.printStackTrace();
      return "none";
     }
    return json.parseAiResponse(history, response.body());
  }


  @Override
  public String request(String json) {
        request = HttpRequest.newBuilder()
          .uri(URI.create(apiUrl))
          .header("Authorization", "Bearer " + apiKey)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(rawJson))
          .build();


    try {
      response = http.send(request, HttpResponse.BodyHandlers.ofString());
      logger.info("Request sent to: "+model);

      if (response.statusCode() == 404) { logger.warning("No model: " + model + " found, check your model name (404 ERROR)"); return "none"; }

      if (response.statusCode() == 429) { logger.warning("Model quota exceeded, model: " + model + ", try again later (429 ERROR)"); return "none"; }

      if (response.statusCode() != 200) { logger.warning("Unexpected API error: " + response.statusCode() + "" + response.body()); return "none"; }

      if (response != null) { logger.info("Got response from " + model + " response: " + response); }

    } catch (Exception e) {
      logger.warning(e.toString());
      e.printStackTrace();
      return "none";
     }
    return response.body();
  }
}
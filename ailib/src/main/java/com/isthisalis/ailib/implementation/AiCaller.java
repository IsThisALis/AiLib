package com.isthisalis.ailib.implementation;

import com.isthisalis.ailib.api.ai.AiService;
import com.isthisalis.ailib.api.ai.tools.ToolCallParser;
import com.isthisalis.ailib.exception.ApiException;
import com.isthisalis.ailib.exception.HttpIOException;
import com.isthisalis.ailib.api.Configuration;

import com.isthisalis.ailib.util.JSON;

import com.isthisalis.ailib.util.DTO.request.Message;

import lombok.NonNull;

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
    

  private static final HttpClient HTTP = HttpClient.newHttpClient();
  private volatile HttpRequest request;
  private volatile HttpResponse<String> response;

  private JSON json;

  private Logger logger = Logger.getGlobal();

  private String model;
  private String apiKey;
  private String apiUrl;


  /**
   * Constructor for AI service base implementation class.
   * 
   * @param config Configuration for AI API provider data. 
   * @see com.isthisalis.ailib.api.Configuration.
   * @param toolCallParser Tool Calling processor implementation. 
   * @see com.isthisalis.ailib.api.ai.ToolCallParser.
   */
  public AiCaller(@NonNull Configuration config, ToolCallParser toolCallParser) {
    json = new JSON(config, this, toolCallParser);
    model = config.getModel();
    apiKey = config.getApiKey();
    apiUrl = config.getApiUrl();
  }

  public void update(@NonNull Configuration config, ToolCallParser toolCallParser) {
    if (toolCallParser != null) json = new JSON(config, this, toolCallParser);
    model = config.getModel();
    apiKey = config.getApiKey();
    apiUrl = config.getApiUrl();
  }


    @Override
  public String ask(String message) throws ApiException, HttpIOException {
    String rawJson;
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
      response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
      logger.info(response.body().trim());

      if (response.statusCode() != 200) throw new ApiException(response.statusCode(), response.body());
      if (response != null) { logger.info("Got response from " + model); }

    } catch (Exception e) {
      if (!e.getClass().equals(ApiException.class)) throw new HttpIOException(e.getMessage());
     }
    return json.parseAiResponse(history, response.body());
  }


  @Override
  public String request(String json) throws ApiException, HttpIOException {
        request = HttpRequest.newBuilder()
          .uri(URI.create(apiUrl))
          .header("Authorization", "Bearer " + apiKey)
          .header("Content-Type", "application/json")
          .POST(HttpRequest.BodyPublishers.ofString(json))
          .build();

    try {
      response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
      logger.info("Request sent to: "+model);

      if (response.statusCode() != 200) { throw new ApiException(response.statusCode(), response.body()); }

      if (response != null) { logger.info("Got response from " + model + " response: " + response); }

    } catch (Exception e) {
      if (!e.getClass().equals(ApiException.class)) throw new HttpIOException(e.getMessage());
     }
    return response.body();
  }
}
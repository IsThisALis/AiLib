package com.isthisalis.ailib.implementation;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import java.util.List;
import java.util.logging.Logger;

import com.isthisalis.ailib.api.Configuration;
import com.isthisalis.ailib.api.ai.AiService;
import com.isthisalis.ailib.api.ai.tools.ToolCallParser;

import com.isthisalis.ailib.exception.ApiException;
import com.isthisalis.ailib.exception.HttpIOException;

import com.isthisalis.ailib.util.RequestBuilder;
import com.isthisalis.ailib.util.DTO.Message;
import com.isthisalis.ailib.util.DTO.request.AiRequest;

import lombok.Getter;
import lombok.NonNull;

/**
 * AI functions implementation 
 */
public class AiCaller implements AiService {
    

  private static final HttpClient HTTP = HttpClient.newHttpClient();
  private volatile HttpRequest request;
  private volatile HttpResponse<String> response;

  private @Getter RequestBuilder json;

  private final Logger logger = Logger.getLogger("AiService");

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
    json = new RequestBuilder(config, this, toolCallParser);
    model = config.getModel();
    apiKey = config.getApiKey();
    apiUrl = config.getApiUrl();
  }

  /**
   * Updates AiCaller data.
   * 
   * @param config Configuration for API provider data.
   * @see com.isthisalis.ailib.api.Configuration.
   * @param toolCallParser Tool Calling processor implementation. 
   * @see com.isthisalis.ailib.api.ai.ToolCallParser.
   */
  public void update(@NonNull Configuration config, ToolCallParser toolCallParser) {
    if (toolCallParser != null) json = new RequestBuilder(config, this, toolCallParser);
    model = config.getModel();
    apiKey = config.getApiKey();
    apiUrl = config.getApiUrl();
  }


    /**
     * Sends HTTP query to API provider using API key, URL and AI model.
     * 
     * @param message Message to AI model.
     * @return AI response as String.
     */
    @Override
  public String ask(String message) throws ApiException, HttpIOException {
    AiRequest req;
    List<Message> history = json.createInitialHistory(message);

    try {
      req = json.makeAiRequest(history);
    } catch (Exception e) {
      logger.warning("Error! "+e);
      return "none";
    }
        request = HttpRequest.newBuilder()
          .uri(URI.create(apiUrl))
          .header("Authorization", "Bearer " + apiKey)
          .header("Content-Type", "application/json")
          .header("HTTP-Referer", "https://github.com/IsThisALis/AiLib")
          .header("X-Title", "AiLib")
          .POST(HttpRequest.BodyPublishers.ofString(json.read(req)))
          .build();

    try {
      logger.info("Request sent to: "+model);
      response = HTTP.send(request, HttpResponse.BodyHandlers.ofString());
      request = null;

      if (response.statusCode() != 200) throw new ApiException(response.statusCode(), response.body());
      if (response != null) { logger.info("Got response from " + model); }

    } catch (Exception e) {
      if (!e.getClass().equals(ApiException.class)) throw new HttpIOException(e.getMessage());
     }
    return json.parseAiResponse(history, response.body());
  }


  /**
   * Sends request to AI model, using pre-built JSON.
   * 
   * @param json JSON to be sent as request.
   * @return JSON response as String.
   */
  @Override
  public String request(String json) throws ApiException, HttpIOException {
        request = HttpRequest.newBuilder()
          .uri(URI.create(apiUrl))
          .header("Authorization", "Bearer " + apiKey)
          .header("Content-Type", "application/json")
          .header("HTTP-Referer", "https://github.com/IsThisALis/AiLib")
          .header("X-Title", "AiLib")
          .POST(HttpRequest.BodyPublishers.ofString(json))
          .build();

      try { response = HTTP.send(request, HttpResponse.BodyHandlers.ofString()); }
      catch (Exception e) {
        throw new HttpIOException(e.getMessage());
      }
      request = null;
      if (response.statusCode() != 200) { throw new ApiException(response.statusCode(), response.body()); }

      if (response != null) { logger.info("Got response from " + model); }
      return response.body();
  }
}
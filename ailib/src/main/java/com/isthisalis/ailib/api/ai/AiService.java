package com.isthisalis.ailib.api.ai;

import com.isthisalis.ailib.api.Configuration;
import com.isthisalis.ailib.api.ai.tools.ToolCallParser;

import lombok.NonNull;

/**
 * AiService
 */
public interface AiService {

  /**
   * Should update data in implementation.
   * 
   * @param config New configuration.
   * @param ToolCallParser Tool Calling implementation.
   */
  public void update(@NonNull Configuration config, ToolCallParser ToolCallParser);

  /**
   * Sends message to AI model.
   * 
   * @param message Message to be sent.
   * @return AI response as String.
   */
  public String ask(@NonNull String message);

  /**
   * Sends request to AI model
   * @param json JSON to be sent to AI model.
   * @return AI response as JSON String.
   */
  public String request(@NonNull String json);
}

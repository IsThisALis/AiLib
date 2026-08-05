package com.isthisalis.ailib.api.ai;

import com.isthisalis.ailib.api.Configuration;
import com.isthisalis.ailib.api.ai.tools.ToolCallParser;

import lombok.NonNull;

/**
 * AiService
 */
public interface AiService {

  public void update(@NonNull Configuration config, ToolCallParser ToolCallParser);
  public void update();

  public String ask(@NonNull String message);
  public String request(@NonNull String json);
}

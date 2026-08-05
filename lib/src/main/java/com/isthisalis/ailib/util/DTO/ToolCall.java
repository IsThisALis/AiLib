package com.isthisalis.ailib.util.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * ToolCall
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record ToolCall(String type, Integer index, String id, FunctionCall function) {}

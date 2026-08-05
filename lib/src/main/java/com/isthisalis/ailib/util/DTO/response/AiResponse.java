package com.isthisalis.ailib.util.DTO.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * AiResponse
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public record AiResponse(List<Choice> choices) {}

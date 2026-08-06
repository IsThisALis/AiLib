package com.isthisalis.ailib.util.DTO.request;

import java.util.List;
import java.util.Map;

/**
 * Parameters
 */
public record Parameters(String type, Map<String, Object> properties, List<String> required) {}

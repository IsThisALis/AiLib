package com.isthisalis.ailib.api.ai.tools;

import java.util.List;

import com.isthisalis.ailib.util.DTO.ToolCall;
import com.isthisalis.ailib.util.DTO.request.Message;

public interface ToolCallParser {
    
    Message parseToolCalls(List<ToolCall> messages);
    Message parseToolCalls(ToolCall messages);
}  

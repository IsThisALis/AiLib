package com.isthisalis.ailib.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value @EqualsAndHashCode(callSuper = false)
public class JsonException extends RuntimeException {
    String eMsg;
    String msg;
}

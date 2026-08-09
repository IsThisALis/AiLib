package com.isthisalis.ailib.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value @EqualsAndHashCode(callSuper = false)
public class HttpIOException extends RuntimeException {
    String message;
}

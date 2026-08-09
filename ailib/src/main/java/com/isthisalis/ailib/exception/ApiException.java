package com.isthisalis.ailib.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value @EqualsAndHashCode(callSuper = false)
public class ApiException extends RuntimeException {

    int statusCode;
    String message;
}

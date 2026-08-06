package com.isthisalis.ailib.api;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class Configuration {

    private @NonNull String apiKey;
    private @NonNull String apiUrl;
    private @NonNull String model;

    private @NonNull String rules;
    private @NonNull String bio;
}

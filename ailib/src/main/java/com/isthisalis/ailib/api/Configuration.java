package com.isthisalis.ailib.api;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Configuration {

    private String apiKey;
    private String apiUrl;
    private String model;

    private String rules;
    private String bio;
}

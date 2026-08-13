    package com.isthisalis.ailib.util.DTO.request;

    import com.fasterxml.jackson.annotation.JsonInclude;
    import com.fasterxml.jackson.annotation.JsonProperty;

    import lombok.Builder;
    import lombok.Data;

    @Data
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class RequestParams {
        
        /**
         * Maximum tokens in answer.
         */
        @JsonProperty(value = "max_tokens")
        private Integer maxTokens;

        /**
         * AI creativeness (0.0 – 2.0).
         */
        @Builder.Default
        @JsonProperty(value = "temperature")
        private Float temperature = 0f;

        /**
         * Nucleus sampling. Alternative to temperature (0.0 – 1.0).
         */
        @Builder.Default
        @JsonProperty("top_p")
        private Float topP = 0f;

        /**
         * Penalizes frequent tokens (-2.0 – 2.0).
         */
        @Builder.Default
        @JsonProperty("frequency_penalty")
        private Float frequencyPenalty = 0f;

        /**
         * Penalizes new topics (-2.0 – 2.0).
         */
        @Builder.Default
        @JsonProperty("presence_penalty")
        private Float presencePenalty = 0f;
    }

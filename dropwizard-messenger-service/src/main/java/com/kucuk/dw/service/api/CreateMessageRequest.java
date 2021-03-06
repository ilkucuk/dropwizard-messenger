package com.kucuk.dw.service.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateMessageRequest {

    @JsonProperty("RequestId")
    Long requestId;

    @JsonProperty("Title")
    String title;

    @JsonProperty("Content")
    String content;

    @JsonProperty("Author")
    String author;

    @JsonProperty("Time")
    Long time;

    @JsonProperty("blockingCallPeriod")
    Integer blockingCallPeriod;

    @JsonProperty("SampleDoubleField")
    Double sampleDoubleField;

    @JsonProperty("SampleIntegerField")
    Integer sampleIntegerField;

    @JsonProperty("SampleBooleanField")
    Boolean sampleBooleanField;
}

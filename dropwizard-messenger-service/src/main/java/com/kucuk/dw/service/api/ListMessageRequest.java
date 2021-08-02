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
public class ListMessageRequest {

    @JsonProperty("PageSize")
    int pageSize;

    @JsonProperty("PageToken")
    String pageToken;

    @JsonProperty("Author")
    String author;

    @JsonProperty("blockingCallPeriod")
    Integer blockingCallPeriod;
}

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
public class Message {

    @JsonProperty("MessageId")
    Long messageId;

    @JsonProperty("Title")
    String title;

    @JsonProperty("Content")
    String content;

    @JsonProperty("Author")
    String author;

    @JsonProperty("Time")
    Long time;
}

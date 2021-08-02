package com.kucuk.dw.service.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ListMessageResponse {

    @JsonProperty("HasNext")
    boolean hasNext;

    @JsonProperty("Messages")
    List<Message> messages;
}


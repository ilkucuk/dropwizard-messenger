package com.kucuk.dw.client;

import com.kucuk.dw.service.api.CreateMessageRequest;

import java.time.Instant;

public class MessageServiceTestHelper {

    private final String author;
    private final String title;
    private final String content;
    private final int blockingCallPeriod;

    private long requestId;
    private boolean sampleBoolean = false;
    private double sampleDouble = 1.0d;
    private int sampleInteger = 0;

    public MessageServiceTestHelper(String author, String title, String content, int blockingCallPeriod) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.blockingCallPeriod = blockingCallPeriod;
    }

    public CreateMessageRequest createRequest() {
        requestId++;
        sampleBoolean = !sampleBoolean;
        sampleDouble += 0.001;
        sampleInteger++;

        return CreateMessageRequest.builder()
                .requestId(requestId)
                .author(author)
                .title(title)
                .content(content)
                .time(Instant.now().toEpochMilli())
                .blockingCallPeriod(blockingCallPeriod)
                .sampleBooleanField(sampleBoolean)
                .sampleDoubleField(sampleDouble)
                .sampleIntegerField(sampleInteger)
                .build();
    }
}

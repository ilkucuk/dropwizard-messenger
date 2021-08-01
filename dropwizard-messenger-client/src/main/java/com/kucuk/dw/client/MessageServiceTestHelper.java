package com.kucuk.dw.client;

import com.kucuk.dw.service.api.CreateMessageRequest;

import java.time.Instant;

public class MessageServiceTestHelper {

    private final String author;
    private final String title;
    private final String content;
    private final int sleepPeriod;

    private long requestId;
    private boolean sampleBoolean = false;
    private double sampleDouble = 1.0d;
    private int sampleInteger = 0;

    public MessageServiceTestHelper(String author, String title, String content, int sleepPeriod) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.sleepPeriod = sleepPeriod;
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
                .sleepPeriod(sleepPeriod)
                .sampleBooleanField(sampleBoolean)
                .sampleDoubleField(sampleDouble)
                .sampleIntegerField(sampleInteger)
                .build();
    }
}

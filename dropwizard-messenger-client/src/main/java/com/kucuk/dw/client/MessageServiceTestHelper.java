package com.kucuk.dw.client;

import com.kucuk.dw.service.api.CreateMessageRequest;
import com.kucuk.dw.service.api.ListMessageRequest;

import java.time.Instant;
import java.util.UUID;

public class MessageServiceTestHelper {

    String author = "ikucuk@gmail.com";
    String title = "Sample Message Title";
    String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

    private final int blockingCallPeriod;
    private final int pageSize;

    private long requestId;
    private boolean sampleBoolean = false;
    private double sampleDouble = 1.0d;
    private int sampleInteger = 0;

    public MessageServiceTestHelper(int blockingCallPeriod, int pageSize) {
        this.blockingCallPeriod = blockingCallPeriod;
        this.pageSize = pageSize;
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

    public ListMessageRequest newListMessageRequest() {
        return ListMessageRequest.builder()
                .blockingCallPeriod(blockingCallPeriod)
                .author(author)
                .pageSize(pageSize)
                .pageToken(UUID.randomUUID().toString())
                .build();
    }
}

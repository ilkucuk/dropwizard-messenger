package com.kucuk.dw.client;

import lombok.Builder;
import lombok.Getter;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Closeable;
import java.time.Instant;
import java.util.concurrent.Callable;


public class MessageServiceCaller implements Callable<MessageServiceCaller.CallResult>, Closeable {

    private final int loopCount;
    private final Client client;
    private final String uri;
    private final String author;
    private final String title;
    private final String content;
    private final int sleepPeriod;

    private long requestId;
    private boolean sampleBoolean = false;
    private double sampleDouble = 1.0d;
    private int sampleInteger = 0;

    @Getter
    private CallResult callResult;

    public MessageServiceCaller(int loopCount, String uri, long requestId, String author, String title, String content, int sleepPeriod) {
        this.loopCount = loopCount;
        this.requestId = requestId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.sleepPeriod = sleepPeriod;
        this.client = ClientBuilder.newClient();
        this.uri = uri;
    }


    @Override
    public CallResult call() {

        int success = 0;
        int failure = 0;

        long start = Instant.now().toEpochMilli();
        for(int i=0; i<loopCount; i++) {
            CreateMessageRequest request = createRequest();

            WebTarget target = client.target(uri);
            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));

            if (processResponse(response)) {
                success++;
            } else {
                failure++;
            }
        }
        long duration = Instant.now().toEpochMilli() - start;

        callResult = CallResult.builder()
                .successCount(success)
                .failureCount(failure)
                .duration(duration)
                .build();
        return callResult;
    }

    private boolean processResponse(Response response) {
       return response.getStatus() == 200;
    }

    private CreateMessageRequest createRequest() {
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

    @Override
    public void close() {
        client.close();
    }

    @Builder
    @Getter
    static class CallResult {
        private final int successCount;
        private final int failureCount;
        private final long duration;
    }
}

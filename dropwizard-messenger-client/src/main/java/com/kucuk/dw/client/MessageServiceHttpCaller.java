package com.kucuk.dw.client;

import com.kucuk.dw.service.api.CreateMessageRequest;
import com.kucuk.dw.service.api.CreateMessageResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;

public class MessageServiceHttpCaller implements MessageServiceCaller {

    private final int loopCount;
    private final Client client;
    private final WebTarget target;

    private final String author;
    private final String title;
    private final String content;
    private final int sleepPeriod;

    private long requestId;
    private boolean sampleBoolean = false;
    private double sampleDouble = 1.0d;
    private int sampleInteger = 0;

    private long responseAccumulator = 0;

    public MessageServiceHttpCaller(int loopCount, String uri, long requestId, String author, String title, String content, int sleepPeriod) {
        this.loopCount = loopCount;
        this.requestId = requestId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.sleepPeriod = sleepPeriod;
        this.client = ClientBuilder.newClient();
        target = client.target(uri);
    }


    @Override
    public CallResult call() {

        int success = 0;
        int failure = 0;

        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON)
                 .header("Connection", "keep-alive");

        long start = Instant.now().toEpochMilli();
        for (int i = 0; i < loopCount; i++) {
            CreateMessageRequest request = createRequest();

            Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));

            if (processResponse(response)) {
                success++;
            } else {
                failure++;
            }
        }
        long duration = Instant.now().toEpochMilli() - start;
        client.close();

        return CallResult.builder()
                .successCount(success)
                .failureCount(failure)
                .duration(duration)
                .accumulator(responseAccumulator)
                .build();
    }

    private boolean processResponse(Response response) {
        if (response.getStatus() == 200) {
            CreateMessageResponse createMessageResponse = response.readEntity(CreateMessageResponse.class);
            responseAccumulator += createMessageResponse.getResponseId() > 0 ? 1 : 0;
            return true;
        } else
            return false;
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
}

package com.kucuk.dw.client.caller;

import com.kucuk.dw.client.MessageServiceTestHelper;
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

public class MessageServiceCreateCaller implements MessageServiceCaller {

    private final int loopCount;
    private final MessageServiceTestHelper testHelper;
    private final Client client;
    private final WebTarget target;

    private long responseAccumulator = 0;

    public MessageServiceCreateCaller(int loopCount, MessageServiceTestHelper testHelper, String uri) {
        this.loopCount = loopCount;
        this.testHelper = testHelper;
        this.client = ClientBuilder.newClient();
        target = client.target(uri);
    }

    @Override
    public CallResult call() {

        int success = 0;
        int failure = 0;

        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON)
                .header("Connection", "keep-alive");

        long start = Instant.now().toEpochMilli();
        for (int i = 0; i < loopCount; i++) {
            CreateMessageRequest request = testHelper.createRequest();
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
}

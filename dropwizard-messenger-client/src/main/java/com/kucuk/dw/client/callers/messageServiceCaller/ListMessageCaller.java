package com.kucuk.dw.client.callers.messageServiceCaller;

import com.kucuk.dw.client.MessageServiceTestHelper;
import com.kucuk.dw.client.callers.CallResult;
import com.kucuk.dw.service.api.ListMessageRequest;
import com.kucuk.dw.service.api.ListMessageResponse;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;

public class ListMessageCaller extends MessageServiceCallerBase {

    private final int callCount;
    private final MessageServiceTestHelper testHelper;
    private final Client client;
    private final WebTarget target;

    private long responseAccumulator = 0;

    public ListMessageCaller(int callCount, MessageServiceTestHelper testHelper, String uri) {
        this.callCount = callCount;
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
        for (int i = 0; i < callCount; i++) {
            ListMessageRequest request = testHelper.newListMessageRequest();
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
            ListMessageResponse listMessageResponse = response.readEntity(ListMessageResponse.class);
            responseAccumulator += listMessageResponse.getTime() > 0 ? 1 : 0;
            return true;
        } else
            return false;
    }
}

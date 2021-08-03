package com.kucuk.dw.client.caller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kucuk.dw.client.MessageServiceTestHelper;
import com.kucuk.dw.service.api.ListMessageRequest;
import com.kucuk.dw.service.api.ListMessageResponse;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.client.util.BytesRequestContent;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpMethod;
import org.eclipse.jetty.http2.client.HTTP2Client;
import org.eclipse.jetty.http2.client.http.HttpClientTransportOverHTTP2;

import java.io.IOException;
import java.time.Instant;

public class MessageServiceHttp2ListCaller implements MessageServiceCaller {

    private final int loopCount;
    private final MessageServiceTestHelper testHelper;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final String uri;

    private long responseAccumulator = 0;

    public MessageServiceHttp2ListCaller(int loopCount, MessageServiceTestHelper testHelper, String uri) {
        this.loopCount = loopCount;
        this.testHelper = testHelper;

        this.uri = uri;
        HTTP2Client http2Client = new HTTP2Client();
        httpClient = new HttpClient(new HttpClientTransportOverHTTP2(http2Client));
        objectMapper = new ObjectMapper();
    }

    @Override
    public CallResult call() throws Exception {

        int success = 0;
        int failure = 0;
        httpClient.start();

        long start = Instant.now().toEpochMilli();
        for (int i = 0; i < loopCount; i++) {
            ListMessageRequest listMessageRequest = testHelper.newListMessageRequest();
            byte[] json = objectMapper.writeValueAsBytes(listMessageRequest);

            Request request = httpClient.newRequest(uri)
                    .method(HttpMethod.POST)
                    .header(HttpHeader.CONTENT_TYPE, "application/json")
                    .body(new BytesRequestContent(json));

            ContentResponse response = request.send();
            if (processResponse(response)) {
                success++;
            } else {
                failure++;
            }
        }
        long duration = Instant.now().toEpochMilli() - start;
        httpClient.stop();

        return CallResult.builder()
                .successCount(success)
                .failureCount(failure)
                .duration(duration)
                .accumulator(responseAccumulator)
                .build();
    }

    private boolean processResponse(ContentResponse response) throws IOException {
        if (response.getStatus() == 200) {
            ListMessageResponse listMessageResponse = objectMapper.readValue(response.getContent(), ListMessageResponse.class);
            responseAccumulator += listMessageResponse.getTime() > 0 ? 1 : 0;
            return true;
        } else
            return false;
    }
}

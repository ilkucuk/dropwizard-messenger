package com.kucuk.dw.client;

import com.fasterxml.jackson.databind.ObjectMapper;
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

public class MessageServiceHttp2Caller implements MessageServiceCaller {

    private final int loopCount;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    private final String author;
    private final String title;
    private final String content;
    private final int sleepPeriod;
    private final String uri;
    private final int messageCount;

    private long requestId;
    private boolean sampleBoolean = false;
    private double sampleDouble = 1.0d;
    private int sampleInteger = 0;

    private long responseAccumulator = 0;

    public MessageServiceHttp2Caller(int loopCount, String uri, long requestId, String author, String title, String content, int sleepPeriod, int messageCount) {
        this.loopCount = loopCount;
        this.requestId = requestId;
        this.author = author;
        this.title = title;
        this.content = content;
        this.sleepPeriod = sleepPeriod;

        this.uri = uri;
        HTTP2Client http2Client = new HTTP2Client();
        httpClient = new HttpClient(new HttpClientTransportOverHTTP2(http2Client));
        httpClient.setFollowRedirects(false);
        objectMapper = new ObjectMapper();
        this.messageCount = messageCount;
    }

    @Override
    public CallResult call() throws Exception {

        int success = 0;
        int failure = 0;

        System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
        httpClient.start();

        long start = Instant.now().toEpochMilli();
        for(int i=0; i<loopCount; i++) {
            CreateMessageRequest createMessageRequest = createRequest();
            byte[] json = objectMapper.writeValueAsBytes(createMessageRequest);

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
            CreateMessageResponse createMessageResponse = objectMapper.readValue(response.getContent(), CreateMessageResponse.class);
            responseAccumulator += createMessageResponse.responseId;
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
                .messageCount(messageCount)
                .build();
    }

}

package com.kucuk.dw.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;

public class DropwizardClient {

    public static void main(String[] args) throws JsonProcessingException {

        CreateMessageRequest request = CreateMessageRequest.builder()
                .requestId(12345L)
                .author("ikucuk@gmail.com")
                .title("Sample Message Title")
                .content("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.")
                .time(Instant.now().toEpochMilli())
                .sleepPeriod(-2000)
                .sampleBooleanField(true)
                .sampleDoubleField(3.14d)
                .sampleIntegerField(12345)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString
        String jsonString = mapper.writeValueAsString(request);
        System.out.println(jsonString);

        Client client = ClientBuilder.newClient();
        WebTarget target = client.target("https://kucuk.com").path("message");
        Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));

        System.out.println(response.getStatus());
        CreateMessageResponse entity = response.readEntity(CreateMessageResponse.class);
        System.out.println(entity.getHash());

        Arguments arguments = new Arguments();
        arguments.addArgument("host", "kucuk.com");
        arguments.addArgument("port", "443");
        arguments.addArgument("requestId", "1000");
        arguments.addArgument("author", "ikucuk@gmail.com");
        arguments.addArgument("title", "Message Title For Medium Message");
        arguments.addArgument("content", "Message Content");
        arguments.addArgument("sleepPeriod", "100");
        JavaSamplerContext context = new JavaSamplerContext(arguments);
        DropwizardServiceSampler sampler = new DropwizardServiceSampler();
        sampler.setupTest(context);
        sampler.runTest(context);
    }
}

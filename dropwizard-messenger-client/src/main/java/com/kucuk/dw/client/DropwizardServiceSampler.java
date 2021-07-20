package com.kucuk.dw.client;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;

public class DropwizardServiceSampler extends AbstractJavaSamplerClient {

    Client client = ClientBuilder.newClient();
    WebTarget target;

    @Override
    public void setupTest(JavaSamplerContext context) {
        String host = context.getParameter("host");
        int port = Integer.parseInt(context.getParameter("port"));
        target = client.target("https://"+ host +":" + port).path("message");
        super.setupTest(context);
    }

    @Override
    public void teardownTest(JavaSamplerContext context) {
        client.close();
    }

    @Override
    public Arguments getDefaultParameters() {
        Arguments defaultParameters = new Arguments();
        defaultParameters.addArgument("host", "kucuk.com");
        defaultParameters.addArgument("port", "443");
        defaultParameters.addArgument("requestId", "1000");
        defaultParameters.addArgument("author", "ilker.kucuk@oracle.com");
        defaultParameters.addArgument("title", "Quest For Performance");
        defaultParameters.addArgument("content", "I don't know what I am doing :(");
        defaultParameters.addArgument("sleepPeriod", "0");
        return defaultParameters;
    }

    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult result = new SampleResult();
        result.sampleStart();

        try {
            long requestId = Integer.parseInt(javaSamplerContext.getParameter("requestId"));
            String author = javaSamplerContext.getParameter("author");
            String title = javaSamplerContext.getParameter("title");
            String content = javaSamplerContext.getParameter("content");
            int sleepPeriod = Integer.parseInt(javaSamplerContext.getParameter("sleepPeriod"));

            Invocation.Builder invocationBuilder = target.request(MediaType.APPLICATION_JSON);
            CreateMessageRequest request = CreateMessageRequest.builder()
                    .requestId(requestId)
                    .author(author)
                    .title(title)
                    .content(content)
                    .time(Instant.now().toEpochMilli())
                    .sleepPeriod(sleepPeriod)
                    .sampleBooleanField(true)
                    .sampleDoubleField(3.14d)
                    .sampleIntegerField(12345)
                    .build();

            invocationBuilder.header("Content-type", "application/json");
            Response response = invocationBuilder.post(Entity.entity(request, MediaType.APPLICATION_JSON));
            result.sampleEnd();
            result.setSuccessful(response.getStatus() == 200);
            CreateMessageResponse entity = response.readEntity(CreateMessageResponse.class);
            result.setResponseData(entity.getHash(), "UTF-8");
            result.setResponseCodeOK(); // 200 code

        } catch (Exception e) {
            result.sampleEnd(); // stop stopwatch
            result.setSuccessful(false);
            result.setResponseMessage("Exception: " + e);
            // get stack trace as a String to return as document data
            java.io.StringWriter stringWriter = new java.io.StringWriter();
            e.printStackTrace(new java.io.PrintWriter(stringWriter));
            result.setResponseData(stringWriter.toString().getBytes());
            result.setDataType(SampleResult.TEXT);
            result.setResponseCode("500");
        }
        return result;
    }
}

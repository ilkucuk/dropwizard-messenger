package com.kucuk.dw.client;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class DropwizardServiceSampler extends AbstractJavaSamplerClient {

    private MessageServiceCaller caller;

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
        defaultParameters.addArgument("loopCount", "10");
        return defaultParameters;
    }

    @Override
    public void setupTest(JavaSamplerContext context) {
        long requestId = Integer.parseInt(context.getParameter("requestId"));
        String author = context.getParameter("author");
        String title = context.getParameter("title");
        String content = context.getParameter("content");
        int sleepPeriod = Integer.parseInt(context.getParameter("sleepPeriod"));

        String host = context.getParameter("host");
        int port = Integer.parseInt(context.getParameter("port"));
        String uri = "https://"+ host +":" + port + "/message";

        int loopCount = Integer.parseInt(context.getParameter("loopCount"));

        caller = new MessageServiceCaller(loopCount, uri, requestId, author, title, content, sleepPeriod);

        super.setupTest(context);
    }

    /* Implements JavaSamplerClient.teardownTest(JavaSamplerContext) */
    @Override
    public void teardownTest(JavaSamplerContext context) {
        caller.close();
        super.teardownTest(context);
    }


    @Override
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult result = new SampleResult();
        result.sampleStart();

        try {
            MessageServiceCaller.CallResult callResult = caller.call();

            result.sampleEnd();
            result.setSuccessful(callResult.getSuccessCount() > 0);
            result.setResponseData(callResult.getSuccessCount() + " " + callResult.getDuration(), "UTF-8");
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

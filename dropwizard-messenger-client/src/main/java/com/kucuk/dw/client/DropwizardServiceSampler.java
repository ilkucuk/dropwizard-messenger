package com.kucuk.dw.client;

import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;

public class DropwizardServiceSampler extends AbstractJavaSamplerClient {

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
    public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
        SampleResult result = new SampleResult();
        result.sampleStart();

        try {
            long requestId = Integer.parseInt(javaSamplerContext.getParameter("requestId"));
            String author = javaSamplerContext.getParameter("author");
            String title = javaSamplerContext.getParameter("title");
            String content = javaSamplerContext.getParameter("content");
            int sleepPeriod = Integer.parseInt(javaSamplerContext.getParameter("sleepPeriod"));

            String host = javaSamplerContext.getParameter("host");
            int port = Integer.parseInt(javaSamplerContext.getParameter("port"));
            String uri = "https://"+ host +":" + port + "/message";

            int loopCount = Integer.parseInt(javaSamplerContext.getParameter("loopCount"));

            MessageServiceCaller caller = new MessageServiceCaller(loopCount, uri, requestId, author, title, content, sleepPeriod);
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

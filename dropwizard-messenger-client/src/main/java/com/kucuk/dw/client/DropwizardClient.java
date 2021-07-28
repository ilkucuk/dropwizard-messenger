package com.kucuk.dw.client;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class DropwizardClient {

    public static void main(String[] args) throws Exception {

        int sleepPeriod = -100;
        int threadCount = 1;
        int callCount = 200;
        String type = "http1";
        int loop = 1;
        int messageCount = 100;

        if (args.length > 0) {
            sleepPeriod = Integer.parseInt(args[0]);
        }
        if (args.length > 1) {
            threadCount = Integer.parseInt(args[1]);
        }
        if (args.length > 2) {
            callCount = Integer.parseInt(args[2]);
        }
        if (args.length > 3) {
            type = args[3];
        }
        if (args.length > 4) {
            loop = Integer.parseInt(args[4]);
        }
        if (args.length > 5) {
            messageCount = Integer.parseInt(args[5]);
        }

        System.out.println("Protocol: " + type + " MessageCount: " + messageCount + " SleepPeriod: " + sleepPeriod + " ThreadCount: " + threadCount + " CallCount: " + callCount + " loop: " + loop);

        String uri = "https://kucuk.com/message";
        String author = "ikucuk@gmail.com";
        String title = "Sample Message Title";
        String content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";

        for(int j=0; j<loop; j++) {
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            List<Future<CallResult>> results = new ArrayList<>(threadCount);

            for (int i = 0; i < threadCount; i++) {
                MessageServiceCaller caller;
                if (type.equals("http2")) {
                    caller = new MessageServiceHttp2Caller(callCount, uri, 12345L, author, title, content, sleepPeriod, messageCount);
                } else {
                    caller = new MessageServiceHttpCaller(callCount, uri, 12345L, author, title, content, sleepPeriod, messageCount);
                }
                Future<CallResult> callResultFuture = executor.submit(caller);
                results.add(callResultFuture);
            }
            executor.shutdown();
            if (executor.awaitTermination(90, TimeUnit.MINUTES)) {
                executor.shutdownNow();
            }
            double total = 0;

            int totalAccumulator = 0;
            int totalSuccess = 0;
            int totalFailure = 0;
            for (Future<CallResult> resultFuture : results) {
                CallResult result = resultFuture.get();
                total += ((double) result.getDuration()) / callCount;
                totalAccumulator += result.getAccumulator();
                totalSuccess+= result.getSuccessCount();
                totalFailure+= result.getFailureCount();
            }
            System.out.println("Average Call Duration: " + total / results.size() + " Success: " + totalSuccess + " Failure: " + totalFailure + " AC: "+ totalAccumulator );
        }
    }
}

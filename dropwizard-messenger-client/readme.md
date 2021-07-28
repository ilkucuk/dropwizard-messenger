##How to run the load generator 

### java -jar target/dropwizard-messenger-client-1.0-SNAPSHOT.jar [ms] [thread_count] [call_count] [protocol] [repeat] [messageCount]


[ms] : milliseconds of how much service should sleep; if negative it will be forwarded to he second service after multiplied with -1

[thread_count] : calling thread count

[call_count] : how many calls should be made with one http client

[protocol] : http1 or http2

[repeat] : how many times to repeat the load test

[messageCount]: number of messages(id, time, content) returned during the call. 

Ex
```
java -jar dropwizard-messenger-client-1.0-SNAPSHOT.jar -10 20 100 http1 5 1
Protocol: http1 MessageCount: 1 SleepPeriod: -10 ThreadCount: 20 CallCount: 100 loop: 3
Average Call Duration: 769.7165 Success: 2000 Failure: 0 AC: 1433119262
Average Call Duration: 690.0190000000001 Success: 2000 Failure: 0 AC: 1579209561
Average Call Duration: 670.1859999999999 Success: 2000 Failure: 0 AC: 1717269710
```
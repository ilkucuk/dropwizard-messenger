How to run the load generator 

java -jar target/dropwizard-messenger-client-1.0-SNAPSHOT.jar [ms] [thread_count] [call_count] [protocol] [repeat]

[ms] : miliseconds of how much service should sleep; if negative it will be forwarded to he second service after multiplied with -1
[thread_count] : calling thread count
[call_count] : how many calls should be made with one http client
[protocol] : http1 or http2
[repeat] : how many times to repeat the load test

Ex
```
dropwizard-messenger-client % java -jar target/dropwizard-messenger-client-1.0-SNAPSHOT.jar -100 400 200 http1 3
SleepPeriod: -100 ThreadCount: 400 CallCount: 200 loop: 3
Average Call Duration: 475.14076249999994 Success: 80000 Failure: 0 AC: -1481311414
Average Call Duration: 455.854375 Success: 80000 Failure: 0 AC: 2117694476
Average Call Duration: 455.9126125000004 Success: 80000 Failure: 0 AC: 1461026781```
##How to run the load generator 

### java -jar target/dropwizard-messenger-client-1.0-SNAPSHOT.jar test-config.yml

test-config.yml : test configuration file


blockingCallPeriod: if >0 the service will make a blocking call that will block for X milliseconds
ConcurrentClientThreadCount: Number of concurrent client threads
CallCountForASingleClient: A single Http Client object makes N calls, after being initialized and before destroyed
Caller: The Specific Service Caller implementation 
NumberOfRuns: How many times repeat the test
PageSize: Determines the size of payload, 1: is an object of size 399B, 100: 21KB, 1000:216KB

Ex
```
ikucuk@ikucuk-mac dropwizard-messenger-client % java -jar target/dropwizard-messenger-client-1.0-SNAPSHOT.jar test-run-compare-payload.yml

blockingCallPeriod: 100 ConcurrentClientThreadCount: 100 CallCountForASingleClient: 200 CallerMessageServiceHttp2ListCaller NumberOfRuns: 5 PageSize: 100
Average Call Duration: 118.51004999999999 Success: 20000 Failure: 0 AC: 20000
Average Call Duration: 118.69675 Success: 20000 Failure: 0 AC: 20000
Average Call Duration: 121.99095 Success: 20000 Failure: 0 AC: 20000
Average Call Duration: 119.45045 Success: 20000 Failure: 0 AC: 20000
Average Call Duration: 121.25285 Success: 20000 Failure: 0 AC: 20000
```
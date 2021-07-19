# dropwizard-messenger

Simple Dropwizard application for load testing

## How to Run

sudo java -jar dropwizard-messenger-service-1.0-SNAPSHOT.jar server config.yml 

## Sample Request

```{ "RequestId":1625466189714,
"Title":"Message Title",
"Content":"Message Content",
"Author":"ikucuk@gmail.com",
"Time":1625466189714,
"SleepPeriod": -100,
"SampleDoubleField":3.14,
"SampleIntegerField":12345,
"SampleBooleanField":true
}

```



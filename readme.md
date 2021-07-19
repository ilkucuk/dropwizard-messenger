# dropwizard-messenger

Simple Dropwizard application for load testing

## How to Run

sudo java -jar dropwizard-messenger-service-1.0-SNAPSHOT.jar server config.yml 

## Update /etc/hosts file 

```
[ip of the host]	kucuk.com
[ip of the host]	kucuk2.com
ex:
192.168.56.4	kucuk.com
192.168.56.5	kucuk2.com

```

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

# How to Run Load Test

jmeter -n -t dw-load.jmx -l run1.log -e -o run1



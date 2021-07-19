Create a private key
$ openssl genrsa -out server.key 2048

Create certificate signing request
openssl req -new -sha256 -key server2.key -out server2.csr


openssl pkcs8 -topk8 -nocrypt -in kucuk1.com.key > kucuk1.com.pem

Crate cert singed by the CA
openssl x509 -req -days 365 -sha256 -in server2.csr -CA ca.crt -CAkey ca.key -set_serial 1 -out server2.crt

Add Cert to keystore
openssl pkcs12 -export -in kucuk.com.crt -inkey server.pem -name kucuk -out keystore.p12

Add CA Cert to keystore
keytool -import -alias bundle -trustcacerts -file ca.crt -keystore keystore.p12

import ca cert to jvm
cd /Library/Java/JavaVirtualMachines/jdk1.8.0_291.jdk/Contents/Home/jre/lib/security
sudo keytool -import  -trustcacerts -alias kucuk.com -file ~/workspace/dropwizard-messenger/cert/ca.crt -keystore cacerts
/Library/Java/JavaVirtualMachines/jdk-11.0.11.jdk/Contents/Home/lib/security
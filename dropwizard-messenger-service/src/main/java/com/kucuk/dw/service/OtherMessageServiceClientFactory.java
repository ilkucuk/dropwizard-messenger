package com.kucuk.dw.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class OtherMessageServiceClientFactory {

    private static Client client = ClientBuilder.newClient();
    private static WebTarget target = client.target("https://kucuk2.com").path("message");

    public static WebTarget getTarget() {
        return target;
    }
}

package com.kucuk.dw.service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

public class BlockingServiceClientFactory {

    private static final Client client = ClientBuilder.newClient();

    public static WebTarget getTarget(Integer blockMilliseconds) {
        return client
                .target("https://kucuk2.com")
                .path("block")
                .path(String.valueOf(blockMilliseconds));
    }
}

package com.kucuk.dw.service;

import com.kucuk.dw.service.resources.BlockingResource;
import com.kucuk.dw.service.resources.MessageResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class DropwizardServerApplication extends Application<DropwizardServerConfiguration> {

    public static void main(final String[] args) throws Exception {
        new DropwizardServerApplication().run(args);
    }

    @Override
    public String getName() {
        return "dropwizard-server";
    }

    @Override
    public void initialize(final Bootstrap<DropwizardServerConfiguration> bootstrap) {
        // TODO: application initialization
    }

    @Override
    public void run(final DropwizardServerConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(MessageResource.class);
        environment.jersey().register(BlockingResource.class);
        System.out.println("Service Started... v1");
    }

}

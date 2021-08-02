package com.kucuk.dw.service;

import com.kucuk.dw.service.resources.MessageResource;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.commons.text.RandomStringGenerator;

public class DropwizardServerApplication extends Application<DropwizardServerConfiguration> {
    public final static int NO_MESSAGES = 1000;
    public static final int MESSAGE_LEN = 1000;
    public static String[] MESSAGE_CONTENT_ARRAY;

    public static void main(final String[] args) throws Exception {
        new DropwizardServerApplication().run(args);
    }

    @Override
    public String getName() {
        return "dropwizard-server";
    }

    @Override
    public void initialize(final Bootstrap<DropwizardServerConfiguration> bootstrap) {
        generateRandomMessages();
        // TODO: rest of the application initialization here
    }

    @Override
    public void run(final DropwizardServerConfiguration configuration,
                    final Environment environment) {
        environment.jersey().register(MessageResource.class);

        System.out.println("Service Started... v1");
    }

    private void generateRandomMessages() {
        MESSAGE_CONTENT_ARRAY = new String[NO_MESSAGES];
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();

        for (int i = 0; i < NO_MESSAGES; i++) {
            MESSAGE_CONTENT_ARRAY[i] = generator.generate(MESSAGE_LEN);
        }
    }
}

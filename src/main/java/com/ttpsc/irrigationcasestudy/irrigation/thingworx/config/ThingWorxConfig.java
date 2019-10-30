package com.ttpsc.irrigationcasestudy.irrigation.thingworx.config;

import com.thingworx.communications.client.ClientConfigurator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.MessageFormat;

@Configuration
public class ThingWorxConfig {

    @Value("${thingworx.host}")
    private static String host;

    @Value("${thingworx.port}")
    private static String port;

    @Value("${thingworx.appKey}")
    private static String appKey;

    @Bean
    public static ClientConfigurator getConfig() {
        ClientConfigurator config = new ClientConfigurator();

        // Set the URI of the server that we are going to connect to
        config.setUri(MessageFormat.format("ws://{0}:{1}/Thingworx/WS", host, port));

        // Set the ApplicationKey. This will allow the client to authenticate with the server.
        // It will also dictate what the client is authorized to do once connected.
        config.setAppKey(appKey);

        // This will allow us to test against a server using a self-signed certificate.
        // This should be removed for production systems.
        config.ignoreSSLErrors(true); // All self signed certs

        return config;
    }
}

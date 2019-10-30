package com.ttpsc.irrigationcasestudy.irrigation.thingworx.config;

import com.thingworx.communications.client.ClientConfigurator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThingWorxConfig {

    @Bean
    public static ClientConfigurator getConfig() {
        ClientConfigurator config = new ClientConfigurator();

        // Set the URI of the server that we are going to connect to
        config.setUri("ws://192.168.10.128:8080/Thingworx/WS");

        // Set the ApplicationKey. This will allow the client to authenticate with the server.
        // It will also dictate what the client is authorized to do once connected.
        config.setAppKey("35370038-46aa-45ec-b2ff-3d1488ca768c");

        // This will allow us to test against a server using a self-signed certificate.
        // This should be removed for production systems.
        config.ignoreSSLErrors(true); // All self signed certs

        return config;
    }
}

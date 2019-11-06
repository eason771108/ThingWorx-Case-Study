package com.ttpsc.irrigationcasestudy.irrigation.thingworx.client;

import com.thingworx.communications.client.ClientConfigurator;
import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.ttpsc.irrigationcasestudy.irrigation.thingworx.router.IrrigationRouter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import javax.annotation.PostConstruct;

@Configuration
public class IrrigationClient extends ConnectedThingClient {
    private static final Logger LOG = LoggerFactory.getLogger(IrrigationClient.class);

    private static IrrigationRouter irrigationRouter;

    public IrrigationClient(ClientConfigurator config) throws Exception {
        super(config);
    }

    @Autowired
    private ClientConfigurator config;

    private static ClientConfigurator staticConfig;

    @PostConstruct
    private void init() {
        staticConfig = config;
    }

    public static void startRouter() {
        try {

            // Create our client.
            IrrigationClient client = new IrrigationClient(staticConfig);

            // Start the client. The client will connect to the server and authenticate
            // using the ApplicationKey specified above.
            client.start();

            irrigationRouter = new IrrigationRouter("router1", "", client);
            client.bindThing(irrigationRouter);
            // Wait for the client to connect.
            if (client.waitForConnection(30000)) {
                irrigationRouter.bindingAllPropertiesToTWX();
                irrigationRouter.bindingAllServicesToTWX();
                while (!client.isShutdown()) {

                	//update ervery 1 sec
                    Thread.sleep(1000);

                    // Every 1 seconds we tell the thing to process a scan request. This is
                    // an opportunity for the thing to query a data source, update property
                    // values, and push new property values to the server.
                    //
                    // This loop demonstrates how to iterate over multiple VirtualThings
                    // that have bound to a client. In this simple example the things
                    // collection only contains one VirtualThing.
                    for (VirtualThing vt : client.getThings().values()) {
                        vt.processScanRequest();
                    }
                }
            } else {
            	LOG.warn("Client did not connect within 30 seconds. Exiting");
            }

        } catch (Exception e) {
            LOG.error("An exception occurred during execution.", e);
        }

        LOG.info("SimpleThingClient is done. Exiting");
    }

    @Lazy
    @Bean
    public static IrrigationRouter getIrrigationRouter() {
        return irrigationRouter;
    }
}

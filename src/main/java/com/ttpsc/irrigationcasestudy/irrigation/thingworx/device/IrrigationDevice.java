package com.ttpsc.irrigationcasestudy.irrigation.thingworx.device;

import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinitions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ThingworxPropertyDefinitions(properties = {
        @ThingworxPropertyDefinition(name = "WaterPressure", description = "Water pressure in the pump", baseType = "NUMBER", category = "Status", aspects = {
                "isReadOnly:FALSE", "isPersistent:TRUE" }),
        @ThingworxPropertyDefinition(name = "IrrigationStrength", description = "Irrigation Strength", baseType = "NUMBER", category = "Status", aspects = {
                "isReadOnly:FALSE", "isPersistent:TRUE" }),
        @ThingworxPropertyDefinition(name = "Location", description = "Location", baseType = "LOCATION", category = "Status", aspects = {
                "isReadOnly:FALSE", "pushType:VALUE", "isPersistent:TRUE" }),
        @ThingworxPropertyDefinition(name = "IrrigationState", description = "Irrigation State", baseType = "BOOLEAN", category = "Status", aspects = {
                "isReadOnly:FALSE", "defaultValue:TRUE", "isPersistent:TRUE" }),
        @ThingworxPropertyDefinition(name = "AlarmState", description = "Alarm State", baseType = "INTEGER", category = "Status", aspects = {
                "isReadOnly:FALSE", "isLogged:TRUE", "defaultValue:3", "isPersistent:TRUE" }),
        @ThingworxPropertyDefinition(name = "RouterName", description = "Router Name", baseType = "THINGNAME", category = "Status", aspects = {
                "isReadOnly:FALSE", "isPersistent:TRUE" }), })

public class IrrigationDevice extends VirtualThing {
    private static final Logger LOG = LoggerFactory.getLogger(IrrigationDevice.class);
    /**
     *
     */
    private static final long serialVersionUID = -998100303615162221L;


    public String deviceName;
    public IrrigationDevice(String name, String description, ConnectedThingClient client)
            throws Exception {
        super(name, description, client);
        this.initializeFromAnnotations();
        deviceName = name;
        LOG.info(String.format("This device is connected : %s", name));
    }
}


package com.ttpsc.irrigationcasestudy.irrigation.thingworx.router;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.metadata.FieldDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinitions;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import com.thingworx.types.BaseTypes;
import com.thingworx.types.InfoTable;
import com.thingworx.types.collections.ValueCollection;
import com.thingworx.types.primitives.IntegerPrimitive;
import com.thingworx.types.primitives.LocationPrimitive;
import com.thingworx.types.primitives.structs.Location;
import com.ttpsc.irrigationcasestudy.irrigation.model.IrrigationDeviceProperty;
import com.ttpsc.irrigationcasestudy.irrigation.thingworx.client.IrrigationClient;
import com.ttpsc.irrigationcasestudy.irrigation.thingworx.config.HttpConfig;
import com.ttpsc.irrigationcasestudy.irrigation.thingworx.device.IrrigationDevice;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@ThingworxPropertyDefinitions(properties = {

		@ThingworxPropertyDefinition(name = "ConnetedDevices", description = "the number of conneted devices", baseType = "INTEGER", category = "Status", aspects = {
				"isReadOnly:TRUE", "pushType:VALUE", "isPersistent:TRUE"}),
		@ThingworxPropertyDefinition(name = "GeoLocation", description = "Router Location", baseType = "LOCATION", category = "Status", aspects = {
				"isReadOnly:FALSE", "pushType:VALUE", "isPersistent:TRUE" }),
		})

public class IrrigationRouter extends VirtualThing {
	private static final Logger LOG = LoggerFactory.getLogger(IrrigationRouter.class);
	
	//this instance
	private final IrrigationRouter routerObj = this;

	private static final long serialVersionUID = 5738957136321905151L;
	private static final  String CONNECTED_DEVICE = "ConnetedDevices";
	private static final String GEO_LOCATOIN = "GeoLocation";
	
	//router properties
	private Map<String, IrrigationDevice> deviceMap = new HashMap<String, IrrigationDevice>();
	private static int connetedDevices = 0;
	private static Location location;

    @Autowired
    private HttpConfig httpConfig;

	//for mail function
	Properties props;
	Session session;
	
	public int getConnetedDevices() {
		return (int) getProperty(CONNECTED_DEVICE).getValue().getValue();
	}
	
	public void setConnetedDevices() throws Exception {
		setProperty(CONNECTED_DEVICE, new IntegerPrimitive(connetedDevices));
	}
	
	public int getGeoLocation() {
		return (int) getProperty(GEO_LOCATOIN).getValue().getValue();
	}
	
	public void setGeoLocation() throws Exception {
		LocationPrimitive loc = new LocationPrimitive(location);
		setProperty(GEO_LOCATOIN, loc);
	}
	/**
     * A custom constructor. We implement this so we can call initializeFromAnnotations, which
     * processes all of the VirtualThing's annotations and applies them to the object.
     * 
     * @param name The name of the thing.
     * @param description A description of the thing.
     * @param client The client that this thing is associated with.
     * @throws Exception
     */
    public IrrigationRouter(String name, String description, ConnectedThingClient client)
            throws Exception {
        super(name, description, client);
        this.initializeFromAnnotations();

        location = new Location(121.31027, 25.02508);
    }

    @ThingworxServiceDefinition(name = "addNewDevice", description = "Add a new devices to client")
    @ThingworxServiceResult(name = "result", description = "",
            baseType = "INTEGER")
    public int addNewDevice(
            @ThingworxServiceParameter(name = "name",
                    description = "The first addend of the operation",
                    baseType = "STRING") String name,             
            @ThingworxServiceParameter(name = "baseTemplateName",
                    description = "Base template name for the operation",
                    baseType = "STRING") String baseTemplateName)
            throws Exception {
    	
    	//Check if already in Map
    	if(deviceMap.containsKey(name)) {
    		LOG.warn(String.format("Device with name(%s) is aready in list", name));
    		return -1;
    	}
    	//crete a thing on TWX platform
    	boolean isSuccessful = this.addNewThingOnThingWorx(baseTemplateName, name);
 
        if (!isSuccessful) {
        	LOG.error("Invoking new thing failed");
        	return -1;
        }

    	IrrigationClient client = (IrrigationClient) this.getClient();
    	
    	IrrigationDevice device = new IrrigationDevice(name, "", routerObj, client);
        
    	deviceMap.put(name, device);
    	
    	//deviceList.add(device);
    	connetedDevices++;
    	setProperty(CONNECTED_DEVICE, new IntegerPrimitive(connetedDevices));
    	
    	//device.bindingAllPropertiesToTWX();
    	//device.bindingAllServicesToTWX();
    	
    	return device.getDeviceListeningPort();
    }
    
    @ThingworxServiceDefinition(name = "getDeviceList", description = "Add a new devices to client")
    @ThingworxServiceResult(name = "result", description = "",
            baseType = "INFOTABLE")
    public InfoTable getDeviceList()
            throws Exception {
    	
    	LOG.info(String.format("List all Devices(%d)", getConnetedDevices()));
    	
    	InfoTable it = new InfoTable();
    	
    	FieldDefinition DeviceNameField = new FieldDefinition();
    	DeviceNameField.setBaseType(BaseTypes.STRING);
    	DeviceNameField.setName("DeviceName");
    	it.addField(DeviceNameField);
    	
    	ValueCollection device;
    	
    	for(Map.Entry<String, IrrigationDevice> entity : deviceMap.entrySet()) {
    		device = new ValueCollection();
    		device.SetValue(DeviceNameField, entity.getValue().getName());
    		it.addRow(device);    		
    	}
    	
    	return it;
    }
    
    /**
     * This method provides a common interface amongst VirtualThings for processing periodic
     * requests. It is an opportunity to access data sources, update property values, push new
     * values to the server, and take other actions.
     */
    @Override
    public void processScanRequest() {

        try {

            // This call evaluates all properties and determines if they should be pushed
            // to the server, based on their pushType aspect. A pushType of ALWAYS means the
            // property will always be sent to the server when this method is called. A
            // setting of VALUE means it will be pushed if has changed since the last
            // push. A setting of NEVER means it will never be pushed.
            //
            // Our Temperature property is set to ALWAYS, so its value will be pushed
            // every time processScanRequest is called. This allows the platform to get
            // periodic updates and store the time series data. Humidity is set to
            // VALUE, so it will only be pushed if it changed.
        	
        	setConnetedDevices();
        	setGeoLocation();
        	
            this.updateSubscribedProperties(1000);

        } catch (Exception e) {
            // This will occur if we provide an unknown property name. We'll ignore
            // the exception in this case and just log it.
            LOG.error("Exception occurred while updating properties.", e);
        }
    }

	public void reportDeviceCurrentProperty(String deviceName,IrrigationDeviceProperty irrigationDeviceProperty) throws Exception {
		if(deviceMap.containsKey(deviceName)) {
			deviceMap.get(deviceName).updateAllProperties(irrigationDeviceProperty.getPumpWaterPressure(),
                    irrigationDeviceProperty.getActualIrrigationPower(),
                    irrigationDeviceProperty.getGeoLocation().orElse(IrrigationDeviceProperty.getDefaultLocation()),
                    irrigationDeviceProperty.getIrrigationState(),
                    irrigationDeviceProperty.getAlarmState(),
                    irrigationDeviceProperty.getIrrigationPowerLevel());
		}
	}
    
    private boolean addNewThingOnThingWorx(String baseTemplate, String deviceName) throws IOException {
        OkHttpClient client = new OkHttpClient();

        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(String.format("{\r\n    \"isSystemObject\": false,\r\n    \"thingTemplate\": \"%s\",\r\n    \"name\": \"%s\"\r\n}", baseTemplate, deviceName), mediaType);
        Request request = new Request.Builder()
                .url(httpConfig.getResourceUrl("/Thingworx/Things"))
                .put(body)
                .addHeader("appKey", httpConfig.getAppKey())
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("Content-Length", "99")
                .addHeader("Connection", "keep-alive")
                .addHeader("cache-control", "no-cache")
                .build();

        Response response = client.newCall(request).execute();
		boolean isSuccessful = response.code() == 200 || response.code() == 409;
		LOG.info("Put thing " + response.toString());
		response.close();
		return isSuccessful;
    }
    
    
    /*
     * close and remove device if it exit in device map
     * */
    public void closeDevice(String deviceName) throws Exception {
    	IrrigationDevice deviceObj = removeDevice(deviceName);
    	
    	if(deviceObj == null)
    		return;
    	
    	deviceObj.close();
    	routerObj.getClient().unbindThing(deviceObj);
    }
    
    /*
     * remover device from device map. This would not close the device.
     * */
    public IrrigationDevice removeDevice(String deviceName) {
		return deviceMap.remove(deviceName);
    }
}

package com.ttpsc.irrigationcasestudy.irrigation.thingworx.device;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.metadata.PropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinitions;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import com.thingworx.relationships.RelationshipTypes.ThingworxEntityTypes;
import com.thingworx.types.collections.ValueCollection;
import com.thingworx.types.primitives.BooleanPrimitive;
import com.thingworx.types.primitives.IPrimitiveType;
import com.thingworx.types.primitives.IntegerPrimitive;
import com.thingworx.types.primitives.LocationPrimitive;
import com.thingworx.types.primitives.NumberPrimitive;
import com.thingworx.types.primitives.StringPrimitive;
import com.thingworx.types.primitives.structs.Location;

@SuppressWarnings("serial")
@ThingworxPropertyDefinitions(properties = {
		@ThingworxPropertyDefinition(name = "PumpWaterPressure", baseType = "NUMBER", aspects = {
				"isPersistent:TRUE" }),
		@ThingworxPropertyDefinition(name = "ActualIrrigationPower", baseType = "NUMBER", aspects = {
				"isPersistent:TRUE" }),
		@ThingworxPropertyDefinition(name = "GeoLocation", baseType = "LOCATION", aspects = { 
				"isPersistent:TRUE" }),
		@ThingworxPropertyDefinition(name = "IrrigationState", baseType = "BOOLEAN", aspects = { 
				"isPersistent:TRUE" }),
		@ThingworxPropertyDefinition(name = "AlarmState", baseType = "INTEGER", aspects = { 
				"isPersistent:TRUE", "defaultValue:-1" }),
		@ThingworxPropertyDefinition(name = "IrrigationPowerLevel", baseType = "INTEGER", aspects = {
				"isPersistent:TRUE" }),
		@ThingworxPropertyDefinition(name = "RouterName", baseType = "STRING", aspects = { 
				"isPersistent:TRUE" })

})
public class IrrigationDevice extends VirtualThing {
	private static final Logger LOG = LoggerFactory.getLogger(IrrigationDevice.class);
	
	private final static String Pump_Water_Pressure = "PumpWaterPressure";
	private final static String Actual_Irrigation_Power = "ActualIrrigationPower";
	private final static String GPS_Geolocation = "GeoLocation";
	private final static String Irrigation_State = "IrrigationState";
	private final static String Alarm_State = "AlarmState";
	private final static String Irrigation_Power_Level = "IrrigationPowerLevel";
	private final static String Router_Name = "RouterName";

	// ThingWorx PropertyValue
	Double PumpWaterPressure;
	Double ActualIrrigationPower;
	Location GeoLocation;
	Boolean IrrigationState;
	Integer AlarmState;
	Integer IrrigationPowerLevel;
	String RouterName;

	public IrrigationDevice(String name, String description, String RouterName, ConnectedThingClient client)
			throws Exception {
		super(name, description, client);
		super.initializeFromAnnotations();
		this.RouterName = RouterName;
		this.setRouterName();
		this.init();
	}

	private void init() throws Exception {
		PumpWaterPressure = getPumpWaterPressure();
		ActualIrrigationPower = getActualIrrigationPower();
		GeoLocation = getGeoLocation();
		IrrigationState = getIrrigationState();
		AlarmState = -1;
		this.setAlarmState();
		IrrigationPowerLevel = getIrrigationPowerLevel();
	}

	public Double getPumpWaterPressure() {
		return (Double) getProperty(Pump_Water_Pressure).getValue().getValue();
	}

	public void setPumpWaterPressure() throws Exception {
		setProperty(Pump_Water_Pressure, new NumberPrimitive(this.PumpWaterPressure));
	}

	public Double getActualIrrigationPower() {
		return (Double) getProperty(Actual_Irrigation_Power).getValue().getValue();
	}

	public void setActualIrrigationPower() throws Exception {
		setProperty(Actual_Irrigation_Power, new NumberPrimitive(this.ActualIrrigationPower));
	}

	public Location getGeoLocation() {
		return (Location) getProperty(GPS_Geolocation).getValue().getValue();
	}

	public void setGeoLocation() throws Exception {
		setProperty(GPS_Geolocation, new LocationPrimitive(this.GeoLocation));
	}

	public Boolean getIrrigationState() {
		return (Boolean) getProperty(Irrigation_State).getValue().getValue();
	}

	public void setIrrigationState() throws Exception {
		setProperty(Irrigation_State, new BooleanPrimitive(IrrigationState));
	}

	public Integer getAlarmState() {
		return (Integer) getProperty(Alarm_State).getValue().getValue();
	}

	public void setAlarmState() throws Exception {
		setProperty(Alarm_State, new IntegerPrimitive(AlarmState));
	}

	public Integer getIrrigationPowerLevel() {
		return (Integer) getProperty(Irrigation_Power_Level).getValue().getValue();
	}

	public void setIrrigationPowerLevel() throws Exception {
		setProperty(Irrigation_Power_Level, new IntegerPrimitive(IrrigationPowerLevel));
	}

	public void setRouterName() throws Exception {
		setProperty(Router_Name, new StringPrimitive(RouterName));
	}

	// processScanRequest
	// twx periodly update data
	@Override
	public void processScanRequest() throws Exception {
		super.processScanRequest();
		this.updateSubscribedProperties(10000);
	}

	// ThingWorx Services
	// TrunOn
	@ThingworxServiceDefinition(name = "turnOnIrrigation")
	@ThingworxServiceResult(name = "result", baseType = "STRING")
	public String turnOnIrrigation() throws Exception {
		LOG.info("Trun On Irrigation System.", IrrigationState);
		this.IrrigationState = true;
		this.setIrrigationState();
		return "Irrigation System Boot.";
	}

	// TurnOff
	@ThingworxServiceDefinition(name = "turnOffIrrigation")
	@ThingworxServiceResult(name = "result", baseType = "STRING")
	public String turnOffIrrigation() throws Exception {
		LOG.info("Turn Off Irrigation System.", IrrigationState);
		this.IrrigationState = false;
		this.setIrrigationState();
		return "Irrigation System Shut down.";
	}

	// Set Pump Water Pressure
	@ThingworxServiceDefinition(name = "setPumpWaterPressure")
	@ThingworxServiceResult(name = "result", baseType = "STRING")
	public String setPumpWaterPressure(
			@ThingworxServiceParameter(name = "PumpWaterPressure", baseType = "NUMBER") Double PumpWaterPressure)
			throws Exception {
		LOG.info("Manually set Pump Water Pressure", PumpWaterPressure);
		this.PumpWaterPressure = PumpWaterPressure;
		System.err.println(this.PumpWaterPressure);
		setPumpWaterPressure();
		System.err.println(getPumpWaterPressure());
		return "Pressure sets to " + PumpWaterPressure;
	}

	// Set Irrigation Power Level
	@ThingworxServiceDefinition(name = "setIrrigationPowerLevel")
	@ThingworxServiceResult(name = "result", baseType = "STRING")
	public String setIrrigationPowerLevel(
			@ThingworxServiceParameter(name = "IrrigationPowerLevel", baseType = "INTEGER") Integer IrrigationPowerLevel)
			throws Exception {
		LOG.info("Manually set Irrigation Power Level", IrrigationPowerLevel);
		this.IrrigationPowerLevel = IrrigationPowerLevel;
		setIrrigationPowerLevel();
		return "Power Level sets to " + IrrigationPowerLevel;
	}

	// Reset Alarm State
	@ThingworxServiceDefinition(name = "resetAlarmState")
	@ThingworxServiceResult(name = "result", baseType = "STRING")
	public String resetAlarmState() throws Exception {
		LOG.info("Reset Alarm State.", AlarmState);
		this.AlarmState = -1;
		this.setAlarmState();
		return "Alarm now is been reset.";
	}

	// TBD
	public void synchronizeState() {
		super.synchronizeState();
		super.syncProperties();
	}

	// Let TWX to overwrite the property
	@Override
	public void processPropertyWrite(PropertyDefinition property, @SuppressWarnings("rawtypes") IPrimitiveType value)
			throws Exception {
		this.setPropertyValue(property.getName(), value);
	}

	public void updateAllProperties(Double PumpWaterPressure, Double ActualIrrigationPower, Location GeoLocation,
			Boolean IrrigationState, Integer AlarmState, Integer IrrigationPowerLevel) throws Exception {
		this.PumpWaterPressure = PumpWaterPressure;
		setPumpWaterPressure();
		this.ActualIrrigationPower = ActualIrrigationPower;
		setActualIrrigationPower();
		this.GeoLocation = GeoLocation;
		setGeoLocation();
		this.IrrigationState = IrrigationState;
		setIrrigationState();
		this.AlarmState = AlarmState;
		setAlarmState();
		this.IrrigationPowerLevel = IrrigationPowerLevel;
		setIrrigationPowerLevel();
	}

	/*
	 * invoke service "SetRemotePropertyBinding" to bind all properties to TWX
	 * */
	public void bindingAllPropertiesToTWX() throws Exception {
		
		ThingworxEntityTypes entityType= ThingworxEntityTypes.Things;
		String thingName = this.getName();
		ConnectedThingClient client = this.getClient();
		
		int size = DevicePropertiesAndServicesEnum.PROPERTIES_STRING.length;
		System.err.println("Size:" + size);
		for(int i = 0; i < size ; i++) {
			ValueCollection vc = new ValueCollection();			
			vc.setValue("propertyName", new StringPrimitive(DevicePropertiesAndServicesEnum.PROPERTIES_STRING[i]));
			vc.setValue("sourcePropertyName", new StringPrimitive(DevicePropertiesAndServicesEnum.PROPERTIES_STRING[i]));
			client.invokeService(entityType , thingName, "SetRemotePropertyBinding" , vc, 30000);
			System.err.println(DevicePropertiesAndServicesEnum.PROPERTIES_STRING[i]);
		}
	}

	/*
	 * invoke service "SetRemoteServiceBinding" to bind all service to TWX
	 * */
	public void bindingAllServicesToTWX() throws Exception {
		ThingworxEntityTypes entityType= ThingworxEntityTypes.Things;
		String thingName = this.getName();
		ConnectedThingClient client = this.getClient();
		
		ValueCollection vc;
		for(String servicesName : DevicePropertiesAndServicesEnum.SERVICES_STRING) {
			vc = new ValueCollection();
			vc.setValue("serviceName", new StringPrimitive(servicesName ));
			vc.setValue("sourceServiceName", new StringPrimitive(servicesName));
			client.invokeService(entityType , thingName, "SetRemoteServiceBinding" , vc, 30000);
		}
	}
}


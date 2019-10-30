package com.ttpsc.irrigationcasestudy.irrigation.thingworx.device;

public class DevicePropertiesAndServicesEnum {
	
	static final public String[] PROPERTIES_STRING = {"PumpWaterPressure"
			,"ActualIrrigationPower","GeoLocation","IrrigationState"
			,"AlarmState","IrrigationPowerLevel","RouterName"};
	
	static public enum PROPERTIES_ENUM {
		_Pump_Water_Pressure,
		_Actual_Irrigation_Power,
		_GPS_Geolocation,
		_Irrigation_State,
		_Alarm_State,
		_Irrigation_Power_Level,
		_Router_Name
	}
	
	static final public String[] SERVICES_STRING = {"turnOnIrrigation"
			,"turnOffIrrigation","setPumpWaterPressure","setIrrigationPowerLevel"
			,"resetAlarmState"};
	
	static public enum SERVICES_ENUM {
		_Turn_On_Irrigation,
		_Turn_Off_Irrigation,
		_Set_PumpWater_Pressure,
		_Set_Irrigation_PowerLevel,
		_Reset_AlarmState
	}
}

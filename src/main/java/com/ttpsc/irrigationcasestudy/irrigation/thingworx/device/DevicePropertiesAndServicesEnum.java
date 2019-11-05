package com.ttpsc.irrigationcasestudy.irrigation.thingworx.device;

public class DevicePropertiesAndServicesEnum {
	
	static final public String[] PROPERTIES_STRING = {"PumpWaterPressure"
			,"ActualIrrigationPower","GeoLocation","IrrigationState"
			,"AlarmState","IrrigationPowerLevel","RouterName", "WeatherStatus"};
	
	static public enum PROPERTIES_ENUM {
		_Pump_Water_Pressure,
		_Actual_Irrigation_Power,
		_GPS_Geolocation,
		_Irrigation_State,
		/** _Alarm_State define
		 * 	Unknown Problem (Code: ERROR-0)
		 * 	No Water (Code: ERROR-1)
		 * 	No Power (Code: ERROR-2)
		 * 	Raining 	(Code: ERROR-3)
		 * */
		_Alarm_State,
		_Irrigation_Power_Level,
		_Router_Name,
		_Weather_Status
	}
	
	static final public String[] SERVICES_STRING = {"turnOnIrrigation"
			,"turnOffIrrigation","setPumpWaterPressure","setIrrigationPowerLevel"
			,"resetAlarmState", "isRain"};
	
	static public enum SERVICES_ENUM {
		_Turn_On_Irrigation,
		_Turn_Off_Irrigation,
		_Set_PumpWater_Pressure,
		_Set_Irrigation_PowerLevel,
		_Reset_AlarmState
	}
}

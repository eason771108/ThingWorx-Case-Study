package com.ttpsc.irrigationcasestudy.irrigation.model;

import com.thingworx.types.primitives.structs.Location;

public class IrrigationDeviceProperty {
    private double pumpWaterPressure;

    private double actualIrrigationPower;

    private Location geoLocation;

    private boolean irrigationState;

    private int alarmState;

    private Integer irrigationPowerLevel;

    public double getPumpWaterPressure() {
        return pumpWaterPressure;
    }

    public void setPumpWaterPressure(double pumpWaterPressure) {
        this.pumpWaterPressure = pumpWaterPressure;
    }

    public double getActualIrrigationPower() {
        return actualIrrigationPower;
    }

    public void setActualIrrigationPower(double actualIrrigationPower) {
        this.actualIrrigationPower = actualIrrigationPower;
    }

    public Location getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(Location geoLocation) {
        this.geoLocation = geoLocation;
    }

    public boolean getIrrigationState() {
        return irrigationState;
    }

    public void setIrrigationState(boolean irrigationState) {
        this.irrigationState = irrigationState;
    }

    public int getAlarmState() {
        return alarmState;
    }

    public void setAlarmState(int alarmState) {
        this.alarmState = alarmState;
    }

    public Integer getIrrigationPowerLevel() {
        return irrigationPowerLevel;
    }

    public void setIrrigationPowerLevel(Integer irrigationPowerLevel) {
        this.irrigationPowerLevel = irrigationPowerLevel;
    }
}

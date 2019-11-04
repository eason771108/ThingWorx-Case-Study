package com.ttpsc.irrigationcasestudy.irrigation.model;

import com.thingworx.types.primitives.structs.Location;

import java.util.Optional;

public class IrrigationDeviceProperty {
    private double pumpWaterPressure;

    private double actualIrrigationPower;

    private Location geoLocation;

    private boolean irrigationState;

    private int alarmState;

    private int irrigationPowerLevel;

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

    public Optional<Location> getGeoLocation() {
        return Optional.ofNullable(geoLocation);
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

    public int getIrrigationPowerLevel() {
        return irrigationPowerLevel;
    }

    public void setIrrigationPowerLevel(Integer irrigationPowerLevel) {
        this.irrigationPowerLevel = irrigationPowerLevel;
    }

    public static Location getDefaultLocation() {
        Location defaultLocation = new Location();

        defaultLocation.setLongitude(0d);
        defaultLocation.setLatitude(0d);
        defaultLocation.setElevation(0d);

        return defaultLocation;
    }
}

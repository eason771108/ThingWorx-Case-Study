package com.ttpsc.irrigationcasestudy.irrigation.service;

import com.ttpsc.irrigationcasestudy.irrigation.model.IrrigationDeviceProperty;

public interface IrrigationRouterService {

    int registerDevice(String deviceName, String baseTemplateName);

    void reportCurrentProperty(String deviceName, IrrigationDeviceProperty irrigationDeviceProperty);
}

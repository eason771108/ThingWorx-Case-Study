package com.ttpsc.irrigationcasestudy.irrigation.service;

public interface IrrigationRouterService {

    int registerDevice(String deviceName, String baseTemplateName);

    boolean reportCurrentProperty();
}

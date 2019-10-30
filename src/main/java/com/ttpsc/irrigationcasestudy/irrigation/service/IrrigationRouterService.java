package com.ttpsc.irrigationcasestudy.irrigation.service;

public interface IrrigationRouterService {

    boolean registerDevice(String deviceName, String baseTemplateName);

    boolean reportCurrentProperty();
}

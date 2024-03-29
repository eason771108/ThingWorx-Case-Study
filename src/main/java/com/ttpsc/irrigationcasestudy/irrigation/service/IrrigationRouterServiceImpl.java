package com.ttpsc.irrigationcasestudy.irrigation.service;

import com.ttpsc.irrigationcasestudy.irrigation.model.IrrigationDeviceProperty;
import com.ttpsc.irrigationcasestudy.irrigation.thingworx.router.IrrigationRouter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class IrrigationRouterServiceImpl implements IrrigationRouterService {
    private static final Logger LOG = LoggerFactory.getLogger(IrrigationRouterServiceImpl.class);

    @Lazy
    @Autowired
    private IrrigationRouter irrigationRouter;

    @Override
    public int registerDevice(String deviceName, String baseTemplateName) {
        int port = -1;
    	try {
    		port = irrigationRouter.addNewDevice(deviceName, baseTemplateName);
        } catch (Exception e) {
            LOG.info("Failed to register a new device!", e);
        }
        return port;
    }

    @Override
    public void reportCurrentProperty(String deviceName, IrrigationDeviceProperty irrigationDeviceProperty) {
        try {
            irrigationRouter.reportDeviceCurrentProperty(deviceName, irrigationDeviceProperty);
        } catch (Exception e) {
            LOG.error("Failed to update device property !", e);
        }
    }
}

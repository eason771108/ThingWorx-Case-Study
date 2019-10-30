package com.ttpsc.irrigationcasestudy.irrigation.service;

import com.ttpsc.irrigationcasestudy.irrigation.thingworx.router.IrrigationRouter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class IrrigationRouterServiceImpl implements IrrigationRouterService {

    @Lazy
    @Autowired
    private IrrigationRouter irrigationRouter;

    @Override
    public boolean registerDevice(String deviceName, String baseTemplateName) {
        try {
            irrigationRouter.addNewDevice(deviceName, baseTemplateName);
            return true;
        } catch (Exception e) {
            log.info("Failed to register a new device!", e);
        }
        return false;
    }

    @Override
    public boolean reportCurrentProperty() {
        return false;
    }
}

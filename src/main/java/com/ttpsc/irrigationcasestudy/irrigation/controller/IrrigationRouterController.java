package com.ttpsc.irrigationcasestudy.irrigation.controller;

import com.ttpsc.irrigationcasestudy.irrigation.model.IrrigationDeviceProperty;
import com.ttpsc.irrigationcasestudy.irrigation.service.IrrigationRouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping(value = "/irrigation-router")
public class IrrigationRouterController {

    private IrrigationRouterService irrigationRouterService;

    @Autowired
    public IrrigationRouterController(IrrigationRouterService irrigationRouterService) {
        this.irrigationRouterService = irrigationRouterService;
    }

    @PostMapping(value = "/devices")
    public boolean registerNewDevice(@RequestBody HashMap<String, String> requestBody) {
        return irrigationRouterService.registerDevice(requestBody.get("deviceName"),
                requestBody.get("baseTemplateName"));
    }

    @PatchMapping(value = "/devices/{deviceName}/property")
    public ResponseEntity reportCurrentProperty(@PathVariable String deviceName,
                                                @RequestBody IrrigationDeviceProperty irrigationDeviceProperty) {
        irrigationRouterService.reportCurrentProperty(deviceName, irrigationDeviceProperty);

        return ResponseEntity.accepted().build();
    }
}

package com.ttpsc.irrigationcasestudy.irrigation.controller;

import com.ttpsc.irrigationcasestudy.irrigation.service.IrrigationRouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(value = "/irrigation-router")
public class IrrigationRouterController {

    private IrrigationRouterService irrigationRouterService;

    @Autowired
    public IrrigationRouterController(IrrigationRouterService irrigationRouterService) {
        this.irrigationRouterService = irrigationRouterService;
    }

    @PostMapping(value = "/device")
    public void registerNewDevice(@RequestBody HashMap<String, String> requestBody) {
        boolean isSuccessful = irrigationRouterService.registerDevice(requestBody.get("deviceName"),
                requestBody.get("baseTemplateName"));
    }
}

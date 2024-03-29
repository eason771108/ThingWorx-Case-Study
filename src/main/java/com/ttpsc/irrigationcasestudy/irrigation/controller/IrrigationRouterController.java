package com.ttpsc.irrigationcasestudy.irrigation.controller;

import com.ttpsc.irrigationcasestudy.irrigation.model.IrrigationDeviceProperty;
import com.ttpsc.irrigationcasestudy.irrigation.service.IrrigationRouterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
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
    public ResponseEntity<Object> registerNewDevice(@RequestBody HashMap<String, String> requestBody) {
        int port = irrigationRouterService.registerDevice(requestBody.get("deviceName"),
                requestBody.get("baseTemplateName"));

        if(port != -1) {
        	return new ResponseEntity<>(String.format("{\"servicePort\" : %d}", port), HttpStatus.OK);        	
        }
        else 
        	return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PatchMapping(value = "/devices/{deviceName}/property")
    public ResponseEntity reportCurrentProperty(@PathVariable String deviceName,
                                                @RequestBody IrrigationDeviceProperty irrigationDeviceProperty) {
        irrigationRouterService.reportCurrentProperty(deviceName, irrigationDeviceProperty);

        return ResponseEntity.accepted().build();
    }

    private ResponseEntity buildResponseEntity(HttpStatus httpStatus,String bodyPattern,
                                               String... params) {
        String responseBody = MessageFormat.format(bodyPattern, params);
        return new ResponseEntity<>(responseBody, httpStatus);
    }
}

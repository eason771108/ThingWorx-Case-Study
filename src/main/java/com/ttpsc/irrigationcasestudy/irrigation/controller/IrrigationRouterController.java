package com.ttpsc.irrigationcasestudy.irrigation.controller;

import com.ttpsc.irrigationcasestudy.irrigation.service.IrrigationRouterService;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Object> registerNewDevice(@RequestBody HashMap<String, String> requestBody) {
        int port = irrigationRouterService.registerDevice(requestBody.get("deviceName"),
                requestBody.get("baseTemplateName"));
        
        
        if(port != -1) {
        	return new ResponseEntity<>(String.format("{\"servicePort\" : %d}", port), HttpStatus.OK);        	
        }
        else 
        	return new ResponseEntity<>("", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

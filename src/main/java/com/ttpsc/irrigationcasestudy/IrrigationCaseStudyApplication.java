package com.ttpsc.irrigationcasestudy;

import com.ttpsc.irrigationcasestudy.irrigation.thingworx.client.IrrigationClient;
import com.ttpsc.irrigationcasestudy.irrigation.thingworx.router.IrrigationRouter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class IrrigationCaseStudyApplication {

    public static void main(String[] args) {
        SpringApplication.run(IrrigationCaseStudyApplication.class, args);
        IrrigationClient.startRouter();
    }
}

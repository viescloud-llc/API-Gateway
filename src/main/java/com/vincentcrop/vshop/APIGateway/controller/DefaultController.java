package com.vincentcrop.vshop.APIGateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vincentcrop.vshop.APIGateway.service.SwaggerService;
import com.vincentcrop.vshop.APIGateway.util.Time;


@RestController
public class DefaultController {

    @Autowired
    private SwaggerService swaggerService;

    @Value("${spring.profiles.active}")
    private String env = "?";

    @GetMapping("/_status/healthz")
    public String healthCheck() {
        return String.format("API Gateway %s is up and running", env);
    }

    @GetMapping("/time/now")
    public Time timeNow() {
        return Time.now();
    }

    @GetMapping("/swaggers")
    public Object getMethodName() {
        return swaggerService.getAllDocs();
    }
}

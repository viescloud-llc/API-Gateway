package com.vincentcrop.vshop.APIGateway.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public String healthCheck1() {
        return String.format("API Gateway %s is up and running", env);
    }

    @GetMapping("/healthCheck")
    public String healthCheck2() {
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

    @GetMapping("/favicon.ico")
    public ResponseEntity<Resource> getFavicon() throws IOException {
        Resource resource = new ClassPathResource("static/favicon.ico");
        return ResponseEntity.ok().contentType(MediaType.valueOf("image/x-icon")).contentLength(resource.contentLength()).body(resource);
    }
}

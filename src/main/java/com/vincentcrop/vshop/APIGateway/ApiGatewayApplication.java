package com.vincentcrop.vshop.APIGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.vincent.inc.viesspringutils.config.BeanConfig;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ApiGatewayApplication {

	public static void main(String[] args) {
		System.setProperty(BeanConfig.H2_IN_MEMORY_PROPERTY_PATH, "true");
		SpringApplication.run(ApiGatewayApplication.class, args);
	}
}

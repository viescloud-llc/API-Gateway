package com.vincentcrop.vshop.APIGateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vincentcrop.vshop.APIGateway.config.filter.AuthenticationFilter;

@Configuration
public class GatewayConfig 
{
    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder)
    {
        return builder.routes()
            .route(r -> r
                .path("/get")
                .filters(f -> f.addRequestHeader("Hello", "World"))
                .uri("http://httpbin.org:80"))
            .route("AUTHENTICATOR-SERVICE", r -> r
                .path("/authenticator/**")
				.filters(f -> f.stripPrefix(1).filter(authenticationFilter))
                .uri("lb://AUTHENTICATOR-SERVICE"))
            .route("ITEM-SERVICE", r -> r
                .path("/item/**")
				.filters(f -> f.stripPrefix(1).filter(authenticationFilter))
                .uri("lb://ITEM-SERVICE"))
            .build();
    }
}

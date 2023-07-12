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
            //-----------------------------TESTING---------------------------------------
            .route(r -> r
                .path("/get")
                .filters(f -> f.addRequestHeader("Hello", "World"))
                .uri("http://httpbin.org:80"))
            //------------------------AUTHENTICATOR-SERVICE------------------------------
            .route("AUTHENTICATOR-SERVICE", r -> r
                .path("/authenticator/**")
				.filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                .uri("lb://AUTHENTICATOR-SERVICE"))
            //------------------------ITEM-SERVICE---------------------------------------
            .route("ITEM-SERVICE", r -> r
                .path("/item/**")
				.filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                .uri("lb://ITEM-SERVICE"))
            //------------------------GAME-SERVICE---------------------------------------
            .route("VGAME-SERVICE", r -> r
                .path("/vgame/**")
				.filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                .uri("lb://VGAME-SERVICE"))
            //------------------------FILE-MANAGER-SERVICE-------------------------------
            .route("FILE-MANAGER-SERVICE", r -> r
                .path("/file/**")
				.filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                .uri("lb://FILE-MANAGER-SERVICE"))
            .build();
    }
}

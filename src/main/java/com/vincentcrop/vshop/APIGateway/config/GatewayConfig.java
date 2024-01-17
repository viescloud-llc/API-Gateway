package com.vincentcrop.vshop.APIGateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vincentcrop.vshop.APIGateway.config.filter.AuthenticationFilter;
import com.vincentcrop.vshop.APIGateway.config.filter.AuthenticationFilterBypass;
import com.vincentcrop.vshop.APIGateway.config.filter.DefaultEndpointFilter;

@Configuration
public class GatewayConfig {
    @Autowired
    private AuthenticationFilter authenticationFilter;

    @Autowired
    private AuthenticationFilterBypass authenticationFilterBypass;

    @Autowired
    private DefaultEndpointFilter defaultEndpointFilter;

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder.routes()
                // ------------------------AUTHENTICATOR-SERVICE------------------------------
                .route("AUTHENTICATOR-SERVICE", r -> r
                        .path("/authenticator/**")
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://AUTHENTICATOR-SERVICE"))
                // ------------------------ITEM-SERVICE---------------------------------------
                .route("ITEM-SERVICE", r -> r
                        .path("/item/**")
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://ITEM-SERVICE"))
                // ------------------------GAME-SERVICE---------------------------------------
                .route("VGAME-SERVICE", r -> r
                        .path("/vgame/**")
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://VGAME-SERVICE"))
                // ------------------------FILE-MANAGER-SERVICE-------------------------------
                .route("FILE-MANAGER-SERVICE", r -> r
                        .path("/file/**")
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://FILE-MANAGER-SERVICE"))
                // ------------------------SMB-FILE-MANAGER-SERVICE-------------------------------
                .route("SMB-FILE-MANAGER-SERVICE", r -> r
                        .path("/smb/**")
                        .filters(f -> f.filter(authenticationFilterBypass).stripPrefix(1))
                        .uri("lb://SMB-FILE-MANAGER-SERVICE"))
                // ------------------------SATURDAY-SERVICE-------------------------------
                .route("SATURDAY-SERVICE", r -> r
                        .path("/saturday/**")
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://SATURDAY-SERVICE"))
                // ------------------------VENKINS-SERVICE-------------------------------
                .route("SATURDAY-SERVICE", r -> r
                        .path("/venkins/**")
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://VENKINS-SERVICE"))
                // ------------------------RAPHAEL-SERVICE-------------------------------
                .route("RAPHAEL-SERVICE", r -> r
                        .path("/raphael/**")
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://RAPHAEL-SERVICE"))
                // ------------------------DEFAULT-USER-SERVICE-------------------------------
                .route("DEFAULT-USER-SERVICE", r -> r
                        .path("/user")
                        .filters(f -> f.filter(defaultEndpointFilter).stripPrefix(1))
                        .uri("lb://AUTHENTICATOR-SERVICE"))
                .route("DEFAULT-USER-SERVICE", r -> r
                        .path("/auth")
                        .filters(f -> f.filter(defaultEndpointFilter).stripPrefix(1))
                        .uri("lb://AUTHENTICATOR-SERVICE"))
                .route("DEFAULT-USER-SERVICE", r -> r
                        .path("/login")
                        .filters(f -> f.filter(defaultEndpointFilter).stripPrefix(1))
                        .uri("lb://AUTHENTICATOR-SERVICE"))
                .route("DEFAULT-USER-SERVICE", r -> r
                        .path("/register")
                        .filters(f -> f.filter(defaultEndpointFilter).stripPrefix(1))
                        .uri("lb://AUTHENTICATOR-SERVICE"))
                .route("DEFAULT-USER-SERVICE", r -> r
                        .path("/logout")
                        .filters(f -> f.filter(defaultEndpointFilter).stripPrefix(1))
                        .uri("lb://AUTHENTICATOR-SERVICE"))
                .route("DEFAULT-USER-SERVICE", r -> r
                        .path("/setting/data")
                        .filters(f -> f.filter(defaultEndpointFilter).stripPrefix(2))
                        .uri("lb://AUTHENTICATOR-SERVICE"))
                .build();
    }
}

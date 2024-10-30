package com.vincentcrop.vshop.APIGateway.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vincentcrop.vshop.APIGateway.config.filter.AuthenticationFilter;
import com.vincentcrop.vshop.APIGateway.config.filter.AuthenticationFilterBypass;
import com.vincentcrop.vshop.APIGateway.config.filter.DefaultEndpointFilter;
import com.vincentcrop.vshop.APIGateway.model.ServiceEnum;

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
                .route(ServiceEnum.AUTHENTICATOR_SERVICE.getName(), r -> r
                        .path(String.format("/%s/**", ServiceEnum.AUTHENTICATOR_SERVICE.getPrefix()))
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.AUTHENTICATOR_SERVICE.getName()))
                // ------------------------ITEM-SERVICE---------------------------------------
                .route(ServiceEnum.ITEM_SERVICE.getName(), r -> r
                        .path(String.format("/%s/**", ServiceEnum.ITEM_SERVICE.getPrefix()))
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.ITEM_SERVICE.getName()))
                // ------------------------GAME-SERVICE---------------------------------------
                .route(ServiceEnum.VGAME_SERVICE.getName(), r -> r
                        .path(String.format("/%s/**", ServiceEnum.VGAME_SERVICE.getPrefix()))
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.VGAME_SERVICE.getName()))
                // ------------------------FILE-MANAGER-SERVICE-------------------------------
                .route(ServiceEnum.FILE_MANAGER_SERVICE.getName(), r -> r
                        .path(String.format("/%s/**", ServiceEnum.FILE_MANAGER_SERVICE.getPrefix()))
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.FILE_MANAGER_SERVICE.getName()))
                // ------------------------SMB-FILE-MANAGER-SERVICE-------------------------------
                .route(ServiceEnum.SMB_FILE_MANAGER_SERVICE.getName(), r -> r
                        .path(String.format("/%s/**", ServiceEnum.SMB_FILE_MANAGER_SERVICE.getPrefix()))
                        .filters(f -> f.filter(authenticationFilterBypass).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.SMB_FILE_MANAGER_SERVICE.getName()))
                // ------------------------SATURDAY-SERVICE-------------------------------
                .route(ServiceEnum.SATURDAY_SERVICE.getName(), r -> r
                        .path(String.format("/%s/**", ServiceEnum.SATURDAY_SERVICE.getPrefix()))
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.SATURDAY_SERVICE.getName()))
                // ------------------------VENKINS-SERVICE-------------------------------
                .route(ServiceEnum.VENKINS_SERVICE.getName(), r -> r
                        .path(String.format("/%s/**", ServiceEnum.VENKINS_SERVICE.getPrefix()))
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.VENKINS_SERVICE.getName()))
                // ------------------------RAPHAEL-SERVICE-------------------------------
                .route(ServiceEnum.RAPHAEL_SERVICE.getName(), r -> r
                        .path(String.format("/%s/**", ServiceEnum.RAPHAEL_SERVICE.getPrefix()))
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.RAPHAEL_SERVICE.getName()))
                // ------------------------AFFILIATE-MARKETING-SERVICE-------------------------------
                .route(ServiceEnum.AFFILIATE_MARKETING_SERVICE.getName(), r -> r
                        .path(String.format("/%s/**", ServiceEnum.AFFILIATE_MARKETING_SERVICE.getPrefix()))
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.AFFILIATE_MARKETING_SERVICE.getName()))
                // ------------------------OBJECT-STORAGE-MANAGER-SERVICE-------------------------------
                .route(ServiceEnum.OBJECT_STORAGE_MANAGER_SERVICE.getName(), r -> r
                        .path(String.format("/%s/**", ServiceEnum.OBJECT_STORAGE_MANAGER_SERVICE.getPrefix()))
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.OBJECT_STORAGE_MANAGER_SERVICE.getName()))
                // ------------------------DNS-MANAGER-SERVICE-------------------------------
                .route(ServiceEnum.DNS_MANAGER_SERVICE.getName(), r -> r
                        .path(String.format("/%s/**", ServiceEnum.DNS_MANAGER_SERVICE.getPrefix()))
                        .filters(f -> f.filter(authenticationFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.DNS_MANAGER_SERVICE.getName()))
                // ------------------------DEFAULT-USER-SERVICE-------------------------------
                .route("DEFAULT-USER-SERVICE", r -> r
                        .path("/user")
                        .filters(f -> f.filter(defaultEndpointFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.AUTHENTICATOR_SERVICE.getName()))
                .route("DEFAULT-USER-SERVICE", r -> r
                        .path("/auth")
                        .filters(f -> f.filter(defaultEndpointFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.AUTHENTICATOR_SERVICE.getName()))
                .route("DEFAULT-USER-SERVICE", r -> r
                        .path("/login")
                        .filters(f -> f.filter(defaultEndpointFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.AUTHENTICATOR_SERVICE.getName()))
                .route("DEFAULT-USER-SERVICE", r -> r
                        .path("/openId")
                        .filters(f -> f.filter(defaultEndpointFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.AUTHENTICATOR_SERVICE.getName()))
                .route("DEFAULT-USER-SERVICE", r -> r
                        .path("/register")
                        .filters(f -> f.filter(defaultEndpointFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.AUTHENTICATOR_SERVICE.getName()))
                .route("DEFAULT-USER-SERVICE", r -> r
                        .path("/logout")
                        .filters(f -> f.filter(defaultEndpointFilter).stripPrefix(1))
                        .uri("lb://" + ServiceEnum.AUTHENTICATOR_SERVICE.getName()))
                .route("DEFAULT-USER-SERVICE", r -> r
                        .path("/setting/data")
                        .filters(f -> f.filter(defaultEndpointFilter).stripPrefix(2))
                        .uri("lb://" + ServiceEnum.AUTHENTICATOR_SERVICE.getName()))
                .build();
    }
}

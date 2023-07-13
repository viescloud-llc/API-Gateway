package com.vincentcrop.vshop.APIGateway.config.filter;

import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import com.vincentcrop.vshop.APIGateway.util.Http.HttpResponseThrowers;
import com.vincentcrop.vshop.APIGateway.util.splunk.Splunk;

import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class DefaultEndpointFilter implements GatewayFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            ServerHttpRequest request = exchange.getRequest();

            String requestMethod = request.getMethod().name();
            String path = request.getURI().getPath();
            String newPath = path;

            switch(requestMethod) {
                case "GET":
                    switch(path) {
                        case "/user":
                            newPath = String.format("%s%s", newPath, "/users");
                            break;
                        case "/auth":
                            newPath = String.format("%s%s", newPath, "/auth");
                            break;
                        case "/logout":
                            newPath = String.format("%s%s", newPath, "/auth/logout");
                            break;
                        default:
                            HttpResponseThrowers.throwNotFound("No Path found");
                    }
                    break;
                case "POST":
                    switch(path) {
                        case "/login":
                            newPath = String.format("%s%s", newPath, "/auth/login");
                            break;
                        case "/register":
                            newPath = String.format("%s%s", newPath, "/users");
                            break;
                        default:
                            HttpResponseThrowers.throwNotFound("No Path found");
                    }
                    break;
                case "PUT":
                    switch(path) {
                        case "user":
                            newPath = String.format("%s%s", newPath, "/auth/user");
                            break;
                        default:
                            HttpResponseThrowers.throwNotFound("No Path found");
                    }
                    break;
                case "PATCH":
                    switch(path) {
                        case "user":
                            newPath = String.format("%s%s", newPath, "/auth/user");
                            break;
                        default:
                            HttpResponseThrowers.throwNotFound("No Path found");
                    }
                    break;
                default:
                    HttpResponseThrowers.throwNotFound("No Path found");
            }

            request = request.mutate().path(newPath).build();
            return chain.filter(exchange.mutate().request(request).build());
            
        } catch (ResponseStatusException ex) {
            throw ex;
        } catch (Exception ex) {
            Splunk.logError(ex);
            HttpResponseThrowers.throwServerError("server is experiencing some error");
        }

        return chain.filter(exchange);
    }
    
}

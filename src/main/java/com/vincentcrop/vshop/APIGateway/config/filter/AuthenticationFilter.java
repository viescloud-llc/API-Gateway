package com.vincentcrop.vshop.APIGateway.config.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.vincentcrop.vshop.APIGateway.model.authenticator.User;
import com.vincentcrop.vshop.APIGateway.service.AuthenticatorService;

import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter
{
    @Autowired
    private AuthenticatorService authenticatorService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) 
    {
        ServerHttpRequest request = exchange.getRequest();

        String path = request.getURI().getPath();

        if (isNotOpenEndpoint(path)) 
        {
            if (this.isAuthMissing(request))
                return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);

            final String token = this.getAuthHeader(request);

            User user = this.authenticatorService.getUser(token);

            if (!isValidRoute(path, user))
                return this.onError(exchange, "Authorization header is invalid", HttpStatus.UNAUTHORIZED);

            this.populateRequestWithHeaders(exchange, user);
        }
        return chain.filter(exchange);
    }
    

    /*PRIVATE*/

    private boolean isValidRoute(String path, User user)
    {
        if(user == null)
            return false;

        
        
        return true;
    }

    private boolean isNotOpenEndpoint(String path)
    {
        List<String> openApiEndpoints = new ArrayList<>();
        openApiEndpoints.add("/auth/login");

        return openApiEndpoints.parallelStream().noneMatch(e -> {
            return e.contains(path) && e.indexOf(path) == 0;
        });
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) 
    {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        return response.setComplete();
    }

    private String getAuthHeader(ServerHttpRequest request) 
    {
        String[] tokens = request.getHeaders().getOrEmpty("Authorization").get(0).split(" ");
        String token = null;
        if(tokens.length > 1)
            token = tokens[tokens.length - 1];

        return token;
    }

    private boolean isAuthMissing(ServerHttpRequest request) 
    {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, User user) 
    {
        exchange.getRequest().mutate()
                .header("user_id", user.getId() + "")
                .build();
    }
}

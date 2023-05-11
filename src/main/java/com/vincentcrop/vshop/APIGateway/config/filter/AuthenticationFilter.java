package com.vincentcrop.vshop.APIGateway.config.filter;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.server.ServerWebExchange;

import com.vincentcrop.vshop.APIGateway.model.authenticator.Role;
import com.vincentcrop.vshop.APIGateway.model.authenticator.Route;
import com.vincentcrop.vshop.APIGateway.model.authenticator.User;
import com.vincentcrop.vshop.APIGateway.service.AuthenticatorService;
import com.vincentcrop.vshop.APIGateway.util.HttpResponseThrowers;
import com.vincentcrop.vshop.APIGateway.util.splunk.Splunk;

import reactor.core.publisher.Mono;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter
{
    private final String DEFAULT_ROLE_OWNER = "OWNER";
    private final String DEFAULT_ROLE_CO_OWNER = "CO-OWNER";
 
    @Autowired
    private AuthenticatorService authenticatorService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) 
    {
        try
        {
            ServerHttpRequest request = exchange.getRequest();

            String requestMethod = request.getMethod().name();
            String path = request.getURI().getPath();

            final String token = this.getAuthHeader(request);
    
            User user = this.authenticatorService.getUser(token);
    
            if (isNotOpenEndpoint(path, requestMethod)) 
            {
                if (this.isAuthMissing(request))
                    return this.onError(exchange, "Authorization header is missing in request", HttpStatus.UNAUTHORIZED);
    
                if (!isValidRoute(path, requestMethod, user))
                    return this.onError(exchange, "Unauthorized", HttpStatus.UNAUTHORIZED);
            }
            
            if(!ObjectUtils.isEmpty(user))
                this.populateRequestWithHeaders(exchange, user);
        }
        catch(Exception ex)
        {
            Splunk.logError(ex);
            HttpResponseThrowers.throwServerError("server is experiencing some error");
        }

        return chain.filter(exchange);
    }
    

    /*PRIVATE*/

    private boolean isValidRoute(String path, String requestMethod, User user)
    {
        if(user == null || !user.isEnable())
            return false;

        List<Route> routeList = this.authenticatorService.getAllRoute();

        /*
         * match rule:
         * 1 is secure
         * same method
         * same path
         * same role
         * 
         * have extra exact match to prioritize specific path first 
         */

        AtomicBoolean matchRole = new AtomicBoolean();
        boolean anyMatchExact = routeList.parallelStream().anyMatch(e -> {
            String ePath = e.getPath();
            boolean anyMatchExactI = e.isSecure() && requestMethod.equals(e.getMethod()) && ePath.equals(path);
            if(anyMatchExactI)
                matchRole.set(anyMatchRole(e.getRoles(), user.getUserRoles()));
            return anyMatchExactI;
        });
        
        boolean anyMatch = routeList.parallelStream().anyMatch(e -> {
            String ePath = e.getPath();
            return e.isSecure() && requestMethod.equals(e.getMethod()) && Pattern.matches("^" + ePath, path) && anyMatchRole(e.getRoles(), user.getUserRoles());
        });

        if(anyMatchExact)
            return matchRole.get();

        if(anyMatch)
            return anyMatch;

        return user.getUserRoles().parallelStream().anyMatch(r -> r.getName().equals(DEFAULT_ROLE_OWNER) || r.getName().equals(DEFAULT_ROLE_CO_OWNER));
    }

    private boolean anyMatchRole(List<Role> originRole, List<Role> targetRole)
    {
        if(originRole == null || targetRole == null)
            return false;
            
        return originRole.parallelStream().anyMatch(or -> targetRole.parallelStream().anyMatch(tr -> tr.equals(or)));
    }

    private boolean isNotOpenEndpoint(String path, String requestMethod)
    {
        List<Route> routelist = this.authenticatorService.getAllRoute();

        return routelist.parallelStream().noneMatch(e -> {
            String ePath = e.getPath();
            return !e.isSecure() && requestMethod.equals(e.getMethod()) && Pattern.matches("^" + ePath, path);
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

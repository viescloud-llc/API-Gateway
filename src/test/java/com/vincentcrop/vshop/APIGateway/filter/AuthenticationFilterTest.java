package com.vincentcrop.vshop.APIGateway.filter;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.vincentcrop.vshop.APIGateway.config.filter.AuthenticationFilter;
import com.vincentcrop.vshop.APIGateway.model.authenticator.Role;
import com.vincentcrop.vshop.APIGateway.model.authenticator.Route;
import com.vincentcrop.vshop.APIGateway.service.AuthenticatorService;

public class AuthenticationFilterTest {

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    @Mock
    private AuthenticatorService authenticatorService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        Mockito.when(authenticatorService.getAllRoute()).thenReturn(getRouteList());
    }

    private List<Route> getRouteList() {
        List<Route> routes = new ArrayList();
        List<Role> roles = new ArrayList();
        roles.add(Role.builder().level(1).name("NORMAL").build());
        roles.add(Role.builder().level(2).name("ADMIN").build());

        routes.add(Route.builder().path("/1").method("GET").secure(true).roles(roles).build());
        routes.add(Route.builder().path("/2").method("GET").secure(false).roles(roles).build());
        routes.add(Route.builder().path("/1/1").method("GET").secure(true).roles(roles).build());
        routes.add(Route.builder().path("/1/1/1").method("GET").secure(true).roles(roles).build());
    
        return routes;
    }

    @Test
    void routeTest() {
        
    }
    
}

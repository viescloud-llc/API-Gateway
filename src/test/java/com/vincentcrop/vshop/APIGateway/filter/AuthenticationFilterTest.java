package com.vincentcrop.vshop.APIGateway.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.vincentcrop.vshop.APIGateway.config.filter.AuthenticationFilter;
import com.vincentcrop.vshop.APIGateway.model.authenticator.Role;
import com.vincentcrop.vshop.APIGateway.model.authenticator.Route;
import com.vincentcrop.vshop.APIGateway.model.authenticator.User;
import com.vincentcrop.vshop.APIGateway.service.AuthenticatorService;

public class AuthenticationFilterTest {

    @InjectMocks
    private AuthenticationFilter authenticationFilter;

    @Mock
    private AuthenticatorService authenticatorService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        when(authenticatorService.getAllRoute()).thenReturn(getRouteList());
    }

    private List<Route> getRouteList() {
        List<Route> routes = new ArrayList<>();
        List<Role> roles1 = new ArrayList<>();
        roles1.add(Role.builder().level(1).name("NORMAL").build());

        List<Role> roles2 = new ArrayList<>();
        roles2.add(Role.builder().level(2).name("ADMIN").build());

        List<Role> roles3 = new ArrayList<>();
        roles3.add(Role.builder().level(1).name("NORMAL").build());
        roles3.add(Role.builder().level(2).name("ADMIN").build());
        
        routes.add(Route.builder().path("/.*").method("GET").secure(false).roles(roles1).build());

        routes.add(Route.builder().path("/1/.*").method("GET").secure(true).roles(roles1).build());
        routes.add(Route.builder().path("/1/1").method("GET").secure(true).roles(roles1).build());
        routes.add(Route.builder().path("/1/1/1").method("GET").secure(false).roles(roles1).build());
        
        routes.add(Route.builder().path("/2/.*").method("GET").secure(true).roles(roles1).build());
        routes.add(Route.builder().path("/2/valid").method("GET").secure(true).roles(roles2).build());
        
        routes.add(Route.builder().path("/somethingreallylongyouknow").method("GET").secure(true).roles(roles1).build());
        routes.add(Route.builder().path("/something/really/long").method("GET").secure(true).roles(roles1).build());
        return routes;
    }

    private User getNormalUser() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().level(1).name("NORMAL").build());
        return User.builder().enable(true).userRoles(roles).build();
    }

    private User getAdminUser() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().level(2).name("ADMIN").build());
        return User.builder().enable(true).userRoles(roles).build();
    }

    private User getOwnerUser() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().level(2).name("OWNER").build());
        return User.builder().enable(true).userRoles(roles).build();
    }

    private User getDisableNormalUser() {
        List<Role> roles = new ArrayList<>();
        roles.add(Role.builder().level(2).name("ADMIN").build());
        return User.builder().enable(true).userRoles(roles).enable(false).build();
    }

    @Test
    void routePriorityTest() {
        //Secure test
        var valid = this.authenticationFilter.isValidRoute("/something", "GET", null);
        assertTrue(valid);

        valid = this.authenticationFilter.isValidRoute("/1/something", "GET", null);
        assertFalse(valid);

        valid = this.authenticationFilter.isValidRoute("/1/1/1/1", "GET", null);
        assertFalse(valid);

        //specific test
        valid = this.authenticationFilter.isValidRoute("/1/1/1", "GET", getNormalUser());
        assertTrue(valid);

        //long priority test
        valid = this.authenticationFilter.isValidRoute("/2/valid", "GET", null);
        assertFalse(valid);

        valid = this.authenticationFilter.isValidRoute("/2/something", "GET", getAdminUser());
        assertFalse(valid);

        valid = this.authenticationFilter.isValidRoute("/2/valid", "GET", getAdminUser());
        assertTrue(valid);

        valid = this.authenticationFilter.isValidRoute("/2/valid", "GET", getNormalUser());
        assertFalse(valid);

        //disable user check
        valid = this.authenticationFilter.isValidRoute("/something", "GET", getDisableNormalUser());
        assertTrue(valid);

        valid = this.authenticationFilter.isValidRoute("/1/1", "GET", getDisableNormalUser());
        assertFalse(valid);

        //OWNER check
        valid = this.authenticationFilter.isValidRoute("/1/garbage", "GET", getOwnerUser());
        assertTrue(valid);
    }
    
}

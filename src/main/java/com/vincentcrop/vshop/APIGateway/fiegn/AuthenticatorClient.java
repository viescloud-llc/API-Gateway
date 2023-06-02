package com.vincentcrop.vshop.APIGateway.fiegn;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.vincentcrop.vshop.APIGateway.model.authenticator.Route;
import com.vincentcrop.vshop.APIGateway.model.authenticator.User;

import feign.Headers;

@FeignClient("AUTHENTICATOR-SERVICE")
@Headers("Content-Type: application/json")
public interface AuthenticatorClient
{
    @GetMapping(value = "/users", consumes = "application/json", produces="application/json")
    public Object getLoginUser(@RequestHeader(required = false, value = "Authorization") String jwt);

    @GetMapping(value = "/users", consumes = "application/json", produces="application/json")
    public User getLoginUserWithCast(@RequestHeader("Authorization") String jwt);

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") int id);
    
    @PostMapping("/auth/any_authority")
    public ResponseEntity<String> hasAnyAuthority(@RequestHeader("Authorization") String jwt, @RequestBody List<String> roles);

    @PostMapping("/auth/all_authority")
    public ResponseEntity<String> hasAllAuthority(@RequestHeader("Authorization") String jwt, @RequestBody List<String> roles);

    @GetMapping("/routes")
    public ResponseEntity<List<Route>> getAllRoutes();

    @PostMapping("/users")
    public Object register(@RequestBody User user);

    @PostMapping("/auth/login")
    public Object login(@RequestBody User user);

    @GetMapping("/auth/logout")
    public void logout(@RequestHeader("Authorization") String jwt);

    @GetMapping("/auth")
    public Object isLogin(@RequestHeader("Authorization") String jwt);
}

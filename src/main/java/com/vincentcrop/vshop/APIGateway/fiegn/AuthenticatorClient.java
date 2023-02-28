package com.vincentcrop.vshop.APIGateway.fiegn;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.vincentcrop.vshop.APIGateway.model.authenticator.Jwt;
import com.vincentcrop.vshop.APIGateway.model.authenticator.User;

import feign.Headers;

@FeignClient("AUTHENTICATOR-SERVICE")
@Headers("Content-Type: application/json")
public interface AuthenticatorClient
{
    @RequestMapping(method = RequestMethod.GET, value = "/users", consumes = "application/json", produces="application/json")
    public User getUser(@RequestHeader(required = false, value = "Authorization") String jwt1);

    @RequestMapping(method = RequestMethod.GET, value = "/users/{id}")
    public ResponseEntity<User> getById(@PathVariable("id") int id);

    @RequestMapping(method = RequestMethod.POST, value = "/auth/login")
    public ResponseEntity<Jwt> login(@RequestBody User user);
    
    @PostMapping("/auth/any_authority")
    public ResponseEntity<String> hasAnyAuthority(@RequestHeader("Authorization") String jwt, @RequestBody List<String> roles);

    @PostMapping("/auth/all_authority")
    public ResponseEntity<String> hasAllAuthority(@RequestHeader("Authorization") String jwt, @RequestBody List<String> roles);
}

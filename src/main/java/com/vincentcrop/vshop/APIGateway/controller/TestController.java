package com.vincentcrop.vshop.APIGateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vincentcrop.vshop.APIGateway.fiegn.AuthenticatorClient;
import com.vincentcrop.vshop.APIGateway.model.authenticator.Jwt;
import com.vincentcrop.vshop.APIGateway.model.authenticator.User;

@RestController
@RequestMapping("test")
public class TestController 
{
    @Autowired
    private AuthenticatorClient authenticatorClient;

    @GetMapping("1")
    public String hello()
    {
        return "hello";
    }    

    @GetMapping("2")
    public ResponseEntity<Jwt> login()
    {
        User user = User.builder().username("Test").password("Test").build();
        
        return authenticatorClient.login(user);
    }    

    @GetMapping("3")
    public User getUser()
    {
        User user = User.builder().username("Test").password("Test").build();

        var response = authenticatorClient.login(user);
        
        Jwt jwt = response.getBody();

        return authenticatorClient.getUser(String.format("Bearer %s", jwt.getJwt()));
    }    
}

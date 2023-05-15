package com.vincentcrop.vshop.APIGateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vincentcrop.vshop.APIGateway.fiegn.AuthenticatorClient;
import com.vincentcrop.vshop.APIGateway.model.authenticator.Jwt;
import com.vincentcrop.vshop.APIGateway.model.authenticator.User;

@RestController
public class DefaultController {

    @Autowired
    private AuthenticatorClient authenticatorClient;

    @PostMapping("/login")
    public ResponseEntity<Jwt> login(@RequestBody User user) {
        return this.authenticatorClient.login(user);
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        return this.authenticatorClient.register(user);
    }
}

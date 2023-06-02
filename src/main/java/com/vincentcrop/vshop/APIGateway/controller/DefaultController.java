package com.vincentcrop.vshop.APIGateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.vincentcrop.vshop.APIGateway.fiegn.AuthenticatorClient;
import com.vincentcrop.vshop.APIGateway.model.authenticator.User;

@RestController
public class DefaultController {

    @Autowired
    private AuthenticatorClient authenticatorClient;

    @Value("${spring.profiles.active}")
	private String env = "?";

    @GetMapping("/_status/healthz")
	public String healthCheck() {
		return String.format("API Gateway %s is up and running", env);
	}

    @GetMapping("/user")
	public Object getLoginUser(@RequestHeader("Authorization") String jwt) {
		return authenticatorClient.getLoginUser(jwt);
	}

    @GetMapping("/logout")
    public void logout(@RequestHeader("Authorization") String jwt) {
        this.authenticatorClient.logout(jwt);
    }

    @GetMapping("/auth")
    public Object isLogin(@RequestHeader("Authorization") String jwt) {
        return this.authenticatorClient.isLogin(jwt);
    }

    @PostMapping("/login")
    public Object login(@RequestBody User user) {
        return this.authenticatorClient.login(user);
    }

    @PostMapping("/register")
    public Object register(@RequestBody User user) {
        return this.authenticatorClient.register(user);
    }
}

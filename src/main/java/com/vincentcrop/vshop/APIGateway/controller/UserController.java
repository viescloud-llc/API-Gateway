package com.vincentcrop.vshop.APIGateway.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.vincentcrop.vshop.APIGateway.fiegn.AuthenticatorClient;
import com.vincentcrop.vshop.APIGateway.model.authenticator.User;

import feign.FeignException;

@RestController
public class UserController {

    @Autowired
    private AuthenticatorClient authenticatorClient;

    @GetMapping("/user")
    public Object getLoginUser(@RequestHeader("Authorization") String jwt) {
        try {
            var user = authenticatorClient.getLoginUser(jwt);
            return user;
        }
        catch(FeignException ex) {
            throw new ResponseStatusException(ex.status(), ex.getMessage(), ex);
        }
    }

    @GetMapping("/logout")
    public void logout(@RequestHeader("Authorization") String jwt) {
        this.authenticatorClient.logout(jwt);
    }

    @GetMapping("/auth")
    public ResponseEntity<?> isLogin(@RequestHeader("Authorization") String jwt) {
        try {
            this.authenticatorClient.isLogin(jwt);
            return ResponseEntity.ok().build();
        } catch (FeignException.BadRequest ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public Object login(@RequestBody User user) {
        try {
            var o = this.authenticatorClient.login(user);
            return o;
        }
        catch(FeignException ex) {
            throw new ResponseStatusException(ex.status(), ex.getMessage(), ex);
        }
    }

    @PostMapping("/register")
    public Object register(@RequestBody User user) {
        try {
            var o = this.authenticatorClient.register(user);
            return o;
        }
        catch(FeignException ex) {
            throw new ResponseStatusException(ex.status(), ex.getMessage(), ex);
        }
    }

    @PutMapping("/user")
    public Object modifyUser(@RequestHeader("Authorization") String jwt, @RequestBody User user) {
        try {
            var loginUser = authenticatorClient.getLoginUserWithCast(jwt);
            var o = this.authenticatorClient.updateUser(loginUser.getId(), user);
            return o;
        }
        catch(FeignException ex) {
            throw new ResponseStatusException(ex.status(), ex.getMessage(), ex);
        }
    }

    @PatchMapping("/user")
    public Object patchUser(@RequestHeader("Authorization") String jwt, @RequestBody User user) {
        try {
            var loginUser = authenticatorClient.getLoginUserWithCast(jwt);
            var o = this.authenticatorClient.patchUser(loginUser.getId(), user);
            return o;
        }
        catch(FeignException ex) {
            throw new ResponseStatusException(ex.status(), ex.getMessage(), ex);
        }
    }
}

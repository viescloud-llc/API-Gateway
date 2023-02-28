package com.vincentcrop.vshop.APIGateway.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.vault.client.WebClientBuilder;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

import com.vincentcrop.vshop.APIGateway.fiegn.AuthenticatorClient;
import com.vincentcrop.vshop.APIGateway.http.HttpResponseThrowers;
import com.vincentcrop.vshop.APIGateway.model.authenticator.User;

import feign.FeignException;

@Service
public class AuthenticatorService 
{
    @Autowired
    private AuthenticatorClient authenticatorClient;

    public User getUser(String jwt)
    {
        User user;
        try
        {
            user = authenticatorClient.getUser(jwt);
        }
        catch(FeignException ex)
        {
            int status = ex.status();
            if(status == 500 || status == 404 || status < 0)
                HttpResponseThrowers.throwServerError("Server encounter unexpected problem");
            return null;
        }

        return user;
    }
    
    public boolean hasAnyAuthority(User user, List<String> roles)
    {
        return user.getUserRoles().parallelStream().anyMatch((ur) -> roles.parallelStream().anyMatch(r -> ur.getRole().getName().equals(r)));
    }
    
    public boolean hasAllAuthority(User user, List<String> roles)
    {
        return roles.stream().allMatch(r -> user.getUserRoles().parallelStream().anyMatch(ur -> ur.getRole().getName().equals(r)));
    }
}

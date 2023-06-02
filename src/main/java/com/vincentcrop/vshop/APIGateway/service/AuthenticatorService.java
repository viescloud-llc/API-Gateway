package com.vincentcrop.vshop.APIGateway.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.vincentcrop.vshop.APIGateway.fiegn.AuthenticatorClient;
import com.vincentcrop.vshop.APIGateway.model.authenticator.Route;
import com.vincentcrop.vshop.APIGateway.model.authenticator.User;
import com.vincentcrop.vshop.APIGateway.util.Http.HttpResponseThrowers;

import feign.FeignException;

@Service
public class AuthenticatorService 
{
    @Autowired
    private AuthenticatorClient authenticatorClient;

    public User getUser(String jwt)
    {
        User user = null;

        try
        {
            user = authenticatorClient.getUser(jwt);
        }
        catch(FeignException ex)
        {
            int status = ex.status();
            if(status == 500 || status == 404 || status < 0)
                HttpResponseThrowers.throwServerError("Server encounter unexpected problem");
        }

        return user;
    }

    public List<Route> getAllRoute()
    {
        try
        {
            List<Route> list = this.authenticatorClient.getAllRoutes().getBody();
            
            if(ObjectUtils.isEmpty(list))
                list = new ArrayList<>();

            return list;
        }
        catch(Exception ex)
        {
            return new ArrayList<>();
        }
    }
    
    public boolean hasAnyAuthority(User user, List<String> roles)
    {
        return user.getUserRoles().parallelStream().anyMatch((ur) -> roles.parallelStream().anyMatch(r -> ur.getName().equals(r)));
    }
    
    public boolean hasAllAuthority(User user, List<String> roles)
    {
        return roles.stream().allMatch(r -> user.getUserRoles().parallelStream().anyMatch(ur -> ur.getName().equals(r)));
    }
}

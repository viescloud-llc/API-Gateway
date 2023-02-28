package com.vincentcrop.vshop.APIGateway.model.authenticator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRole 
{
    public UserRole(Role role)
    {
        this.role = role;
    }

    private int id;

    private Role role;
}

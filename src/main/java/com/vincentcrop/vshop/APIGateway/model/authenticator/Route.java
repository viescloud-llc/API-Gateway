package com.vincentcrop.vshop.APIGateway.model.authenticator;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Route {
    private int id;

    private String path;

    private String method;

    @Builder.Default
    private boolean secure = false;

    private List<Role> roles;
}

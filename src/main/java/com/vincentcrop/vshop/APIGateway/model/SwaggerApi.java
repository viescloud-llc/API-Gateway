package com.vincentcrop.vshop.APIGateway.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwaggerApi {
    private String serviceName;
    private String prefix;
    private List<SwaggerPath> paths;
}

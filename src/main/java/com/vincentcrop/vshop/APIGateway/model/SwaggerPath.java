package com.vincentcrop.vshop.APIGateway.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SwaggerPath {
    private String path;
    private List<SwaggerMethod> method;
}

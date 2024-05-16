package com.vincentcrop.vshop.APIGateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwaggerMethod {
    private String name;
    private String summary;
    private String operationId;
}

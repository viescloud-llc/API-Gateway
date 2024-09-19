package com.vincentcrop.vshop.APIGateway.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FetchResponse {
    private String url;
    private int statusCode;
}

package com.vincentcrop.vshop.APIGateway.fiegn;

import org.springframework.web.bind.annotation.GetMapping;

import com.vincentcrop.vshop.APIGateway.model.ServiceEnum.ServiceEnumConstant;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(ServiceEnumConstant.VGAME_SERVICE)
public interface VGameClient {
    @GetMapping(value = "/v3/api-docs", consumes = "application/json", produces = "application/json")
    public Object getSwaggerDocs();
}

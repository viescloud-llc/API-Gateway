package com.vincentcrop.vshop.APIGateway.fiegn;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import com.vincentcrop.vshop.APIGateway.model.ServiceEnum.ServiceEnumConstant;

@FeignClient(ServiceEnumConstant.OBJECT_STORAGE_MANAGER_SERVICE)
public interface ObjectStorageManagerClient {
    @GetMapping(value = "/v3/api-docs", consumes = "application/json", produces = "application/json")
    public Object getSwaggerDocs();
}

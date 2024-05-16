package com.vincentcrop.vshop.APIGateway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.google.gson.Gson;
import com.vincentcrop.vshop.APIGateway.fiegn.AffiliateMarketingClient;
import com.vincentcrop.vshop.APIGateway.fiegn.AuthenticatorClient;
import com.vincentcrop.vshop.APIGateway.fiegn.FileManagerClient;
import com.vincentcrop.vshop.APIGateway.fiegn.RaphaelClient;
import com.vincentcrop.vshop.APIGateway.fiegn.SaturdayClient;
import com.vincentcrop.vshop.APIGateway.fiegn.SmbFileManagerClient;
import com.vincentcrop.vshop.APIGateway.fiegn.VGameClient;
import com.vincentcrop.vshop.APIGateway.fiegn.VenkinsClient;
import com.vincentcrop.vshop.APIGateway.model.ServiceEnum;
import com.vincentcrop.vshop.APIGateway.model.ServicePrefixEnum;
import com.vincentcrop.vshop.APIGateway.model.SwaggerApi;
import com.vincentcrop.vshop.APIGateway.model.SwaggerMethod;
import com.vincentcrop.vshop.APIGateway.model.SwaggerPath;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.parser.OpenAPIV3Parser;

@Service
public class SwaggerService {
    public static final Gson gson = new Gson();

    @Autowired
    private AffiliateMarketingClient affiliateMarketingClient;

    @Autowired
    private AuthenticatorClient authenticatorClient;
    
    @Autowired
    private FileManagerClient fileManagerClient;

    @Autowired
    private RaphaelClient raphaelClient;

    @Autowired
    private SaturdayClient saturdayClient;

    @Autowired
    private SmbFileManagerClient smbFileManagerClient;

    @Autowired
    private VenkinsClient venkinsClient;

    @Autowired
    private VGameClient vGameClient;

    public Object getAllDocs() {
        List<SwaggerApi> swaggerApis = new ArrayList<>();
        swaggerApis.add(populatePaths(ServiceEnum.AFFILIATE_MARKETING_SERVICE.getName(), ServicePrefixEnum.AFFILIATE_MARKETING_SERVICE.getPrefix(), affiliateMarketingClient.getSwaggerDocs()));
        swaggerApis.add(populatePaths(ServiceEnum.AUTHENTICATOR_SERVICE.getName(), ServicePrefixEnum.AUTHENTICATOR_SERVICE.getPrefix(), authenticatorClient.getSwaggerDocs()));
        swaggerApis.add(populatePaths(ServiceEnum.FILE_MANAGER_SERVICE.getName(), ServicePrefixEnum.FILE_MANAGER_SERVICE.getPrefix(), fileManagerClient.getSwaggerDocs()));
        swaggerApis.add(populatePaths(ServiceEnum.RAPHAEL_SERVICE.getName(), ServicePrefixEnum.RAPHAEL_SERVICE.getPrefix(), raphaelClient.getSwaggerDocs()));
        swaggerApis.add(populatePaths(ServiceEnum.SATURDAY_SERVICE.getName(), ServicePrefixEnum.SATURDAY_SERVICE.getPrefix(), saturdayClient.getSwaggerDocs()));
        swaggerApis.add(populatePaths(ServiceEnum.SMB_FILE_MANAGER_SERVICE.getName(), ServicePrefixEnum.SMB_FILE_MANAGER_SERVICE.getPrefix(), smbFileManagerClient.getSwaggerDocs()));
        swaggerApis.add(populatePaths(ServiceEnum.VENKINS_SERVICE.getName(), ServicePrefixEnum.VENKINS_SERVICE.getPrefix(), venkinsClient.getSwaggerDocs()));
        swaggerApis.add(populatePaths(ServiceEnum.VGAME_SERVICE.getName(), ServicePrefixEnum.VGAME_SERVICE.getPrefix(), vGameClient.getSwaggerDocs()));
        
        return swaggerApis;
    }

    private SwaggerApi populatePaths(String serviceName, String prefix, Object SwaggerDoc) {
        var result = new OpenAPIV3Parser().readContents(gson.toJson(SwaggerDoc));
        var openApi = result.getOpenAPI();
        
        var api = new SwaggerApi();
        api.setServiceName(serviceName);
        api.setPrefix(prefix);
        api.setPaths(new ArrayList<>());
        openApi.getPaths().forEach((originPath, pathItem) -> {
            var pathStr = String.format("/%s%s", prefix, originPath);
            pathStr = pathStr.replaceAll("[{][a-zA-Z1-9]*[}]", ".*");
            SwaggerPath path = new SwaggerPath();
            path.setPath(pathStr);
            path.setMethod(new ArrayList<>());
            isEmpty(pathItem.getGet(), p -> api.getPaths().add(populateSwaggerPath(path, p, "GET")));
            isEmpty(pathItem.getPost(), p -> api.getPaths().add(populateSwaggerPath(path, p, "POST")));
            isEmpty(pathItem.getPut(), p -> api.getPaths().add(populateSwaggerPath(path, p, "PUT")));
            isEmpty(pathItem.getPatch(), p -> api.getPaths().add(populateSwaggerPath(path, p, "PATCH")));
            isEmpty(pathItem.getDelete(), p -> api.getPaths().add(populateSwaggerPath(path, p, "DELETE")));
            isEmpty(pathItem.getHead(), p -> api.getPaths().add(populateSwaggerPath(path, p, "HEAD")));
            isEmpty(pathItem.getOptions(), p -> api.getPaths().add(populateSwaggerPath(path, p, "OPTIONS")));
            isEmpty(pathItem.getTrace(), p -> api.getPaths().add(populateSwaggerPath(path, p, "TRACE")));
        });

        return api;
    }

    private SwaggerPath populateSwaggerPath(SwaggerPath path, Operation operation, String methodName) {
        SwaggerMethod method = new SwaggerMethod();
        method.setSummary(operation.getSummary());
        method.setOperationId(operation.getOperationId());
        method.setName(methodName);
        path.getMethod().add(method);
        return path;
    }

    private static <T> void isEmpty(T object, Consumer<T> supplier) {
        if(!ObjectUtils.isEmpty(object))
            supplier.accept(object);
    }
}

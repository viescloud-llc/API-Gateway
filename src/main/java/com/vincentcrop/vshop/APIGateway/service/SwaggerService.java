package com.vincentcrop.vshop.APIGateway.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.viescloud.llc.viesspringutils.util.DateTime;
import com.vincentcrop.vshop.APIGateway.fiegn.AffiliateMarketingClient;
import com.vincentcrop.vshop.APIGateway.fiegn.AuthenticatorClient;
import com.vincentcrop.vshop.APIGateway.fiegn.DnsManagerClient;
import com.vincentcrop.vshop.APIGateway.fiegn.FileManagerClient;
import com.vincentcrop.vshop.APIGateway.fiegn.ObjectStorageManagerClient;
import com.vincentcrop.vshop.APIGateway.fiegn.RaphaelClient;
import com.vincentcrop.vshop.APIGateway.fiegn.SaturdayClient;
import com.vincentcrop.vshop.APIGateway.fiegn.SmbFileManagerClient;
import com.vincentcrop.vshop.APIGateway.fiegn.VGameClient;
import com.vincentcrop.vshop.APIGateway.fiegn.VenkinsClient;
import com.vincentcrop.vshop.APIGateway.model.ServiceEnum;
import com.vincentcrop.vshop.APIGateway.model.SwaggerApi;
import com.vincentcrop.vshop.APIGateway.model.SwaggerMethod;
import com.vincentcrop.vshop.APIGateway.model.SwaggerPath;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
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

    @Autowired
    private ObjectStorageManagerClient objectStorageManagerClient;

    @Autowired
    private DnsManagerClient dnsManagerClient;

    private List<SwaggerApi> swaggerApisCache;
    private JsonArray postmanApisCache;

    private DateTime lastFetchTime;
    private final int FETCH_INTERVAL = 60; // 60 seconds

    public Object getAllEndpointSumary() {
        populateDocs();
        return swaggerApisCache;
    }

    public String getPostmanCollection() {
        populateDocs();
        JsonObject header = new JsonObject();
        header.addProperty("name", "V-eco");
        header.addProperty("schema", "https://schema.getpostman.com/json/collection/v2.1.0/collection.json");
        JsonObject rootObject = new JsonObject();
        rootObject.add("info", header);
        rootObject.add("item", postmanApisCache);
        return gson.toJson(rootObject);
    }

    private synchronized void populateDocs() {
        if(this.isFetchTime()) {
            initCache();
            doTry(() -> affiliateMarketingClient.getSwaggerDocs(), s -> populatePaths(ServiceEnum.AFFILIATE_MARKETING_SERVICE.getName(), ServiceEnum.AFFILIATE_MARKETING_SERVICE.getPrefix(), s));
            doTry(() -> authenticatorClient.getSwaggerDocs(), s -> populatePaths(ServiceEnum.AUTHENTICATOR_SERVICE.getName(), ServiceEnum.AUTHENTICATOR_SERVICE.getPrefix(), s));
            doTry(() -> fileManagerClient.getSwaggerDocs(), s -> populatePaths(ServiceEnum.FILE_MANAGER_SERVICE.getName(), ServiceEnum.FILE_MANAGER_SERVICE.getPrefix(), s));
            doTry(() -> raphaelClient.getSwaggerDocs(), s -> populatePaths(ServiceEnum.RAPHAEL_SERVICE.getName(), ServiceEnum.RAPHAEL_SERVICE.getPrefix(), s));
            doTry(() -> saturdayClient.getSwaggerDocs(), s -> populatePaths(ServiceEnum.SATURDAY_SERVICE.getName(), ServiceEnum.SATURDAY_SERVICE.getPrefix(), s));
            doTry(() -> smbFileManagerClient.getSwaggerDocs(), s -> populatePaths(ServiceEnum.SMB_FILE_MANAGER_SERVICE.getName(), ServiceEnum.SMB_FILE_MANAGER_SERVICE.getPrefix(), s));
            doTry(() -> venkinsClient.getSwaggerDocs(), s -> populatePaths(ServiceEnum.VENKINS_SERVICE.getName(), ServiceEnum.VENKINS_SERVICE.getPrefix(), s));
            doTry(() -> vGameClient.getSwaggerDocs(), s -> populatePaths(ServiceEnum.VGAME_SERVICE.getName(), ServiceEnum.VGAME_SERVICE.getPrefix(), s));
            doTry(() -> objectStorageManagerClient.getSwaggerDocs(), s -> populatePaths(ServiceEnum.OBJECT_STORAGE_MANAGER_SERVICE.getName(), ServiceEnum.OBJECT_STORAGE_MANAGER_SERVICE.getPrefix(), s));
            doTry(() -> dnsManagerClient.getSwaggerDocs(), s -> populatePaths(ServiceEnum.DNS_MANAGER_SERVICE.getName(), ServiceEnum.DNS_MANAGER_SERVICE.getPrefix(), s));
        }
    }

    private boolean isFetchTime() {
        if(this.lastFetchTime == null) {
            this.lastFetchTime = DateTime.now();
            return true;
        }

        var now = DateTime.now();
        var temp = DateTime.of(this.lastFetchTime);
        temp.plusSeconds(FETCH_INTERVAL);
        if(temp.isBefore(now)) {
            this.lastFetchTime = DateTime.now();
            return true;
        }

        return false;
    }

    private void initCache() {
        this.swaggerApisCache = new ArrayList<>();
        this.postmanApisCache = new JsonArray();
    }

    private void populatePaths(String serviceName, String prefix, Object SwaggerDoc) {
        var result = new OpenAPIV3Parser().readContents(gson.toJson(SwaggerDoc));
        var openApi = result.getOpenAPI();
        populateSwaggerApi(serviceName, prefix, openApi);
        populatePostmanApis(serviceName, prefix, openApi);
    }

    private void populatePostmanApis(String serviceName, String prefix, OpenAPI openAPI) {
        JsonObject collection = convertToPostmanCollection(serviceName, prefix, openAPI);
        postmanApisCache.add(collection);
    }

    private JsonObject convertToPostmanCollection(String collectionName, String prefix, OpenAPI openAPI) {
        JsonObject collection = new JsonObject();
        collection.addProperty("name", collectionName);
        JsonArray itemArray = new JsonArray();

        Paths paths = openAPI.getPaths();
        for (Map.Entry<String, PathItem> entry : paths.entrySet()) {
            String path = entry.getKey();
            PathItem pathItem = entry.getValue();

            for (Map.Entry<PathItem.HttpMethod, Operation> methodEntry : pathItem.readOperationsMap().entrySet()) {
                PathItem.HttpMethod method = methodEntry.getKey();
                Operation operation = methodEntry.getValue();

                JsonObject request = new JsonObject();
                request.addProperty("name", operation.getOperationId());
                request.addProperty("request", path);
                request.addProperty("method", method.name());

                JsonObject requestObject = new JsonObject();
                requestObject.addProperty("method", method.name());
                requestObject.addProperty("header", "Content-Type: application/json");
                requestObject.addProperty("body", "{}"); // Add actual body if available
                requestObject.addProperty("url", path);

                request.add("request", requestObject);

                itemArray.add(request);
            }
        }

        collection.add("item", itemArray);
        return collection;
    }

    private void populateSwaggerApi(String serviceName, String prefix, OpenAPI openApi) {
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
        this.swaggerApisCache.add(api);
    }

    private SwaggerPath populateSwaggerPath(SwaggerPath path, Operation operation, String methodName) {
        SwaggerMethod method = new SwaggerMethod();
        method.setSummary(operation.getSummary());
        method.setOperationId(operation.getOperationId());
        method.setName(methodName);
        path.getMethod().add(method);
        return path;
    }

    private static <T> void isEmpty(T object, Consumer<T> consumer) {
        if(!ObjectUtils.isEmpty(object))
            consumer.accept(object);
    }

    private static <T> void doTry(Supplier<T> suppier, Consumer<T> consumer) {
        try {
            consumer.accept(suppier.get());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

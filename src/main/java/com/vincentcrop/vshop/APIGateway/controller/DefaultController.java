package com.vincentcrop.vshop.APIGateway.controller;

import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactoryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.vincentcrop.vshop.APIGateway.model.FetchResponse;
import com.vincentcrop.vshop.APIGateway.service.SwaggerService;
import com.vincentcrop.vshop.APIGateway.util.Time;

import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.TrustAllStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import static java.lang.Boolean.parseBoolean;


@RestController
public class DefaultController {

    public static final int CONNECT_TIMEOUT_MS = 15000; // 15 seconds
    public static final int READ_TIMEOUT_MS = 15000; // 15 seconds

    RestTemplate restTemplate;

    public DefaultController() throws KeyManagementException, NoSuchAlgorithmException, KeyStoreException {
        CloseableHttpClient httpClient;

        if (parseBoolean(System.getProperty("bypass.ssl.verify"))) {
            httpClient = HttpClients.custom()
                    .setConnectionManager(PoolingHttpClientConnectionManagerBuilder.create()
                            .setSSLSocketFactory(SSLConnectionSocketFactoryBuilder.create()
                                    .setSslContext(SSLContextBuilder.create()
                                            .loadTrustMaterial(TrustAllStrategy.INSTANCE)
                                            .build())
                                    .setHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                                    .build())
                            .build())
                    .build();
        } else {
            httpClient = HttpClients.createDefault();
        }

        HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
        requestFactory.setHttpClient(httpClient);
        requestFactory.setConnectTimeout(CONNECT_TIMEOUT_MS);
        requestFactory.setConnectionRequestTimeout(READ_TIMEOUT_MS);

        restTemplate = new RestTemplate(requestFactory);
    }

    @Autowired
    private SwaggerService swaggerService;

    @Value("${spring.profiles.active}")
    private String env = "?";

    @GetMapping("/_status/healthz")
    public String healthCheck1() {
        return String.format("API Gateway %s is up and running", env);
    }

    @GetMapping("/healthCheck")
    public String healthCheck2() {
        return String.format("API Gateway %s is up and running", env);
    }

    @GetMapping("/time/now")
    public Time timeNow() {
        return Time.now();
    }

    @GetMapping("/swaggers")
    public Object getMethodName() {
        return swaggerService.getAllEndpointSumary();
    }

    @GetMapping(value = "/postman/collection", produces = "application/json")
    public Object getPostmanCollection() {
        return swaggerService.getPostmanCollection();
    }

    // @GetMapping("/check-site/{url}")
    // public ResponseEntity<FetchResponse> checkUrl1(@PathVariable(value = "url") String pathUrl) {
    //     return fetchUrl(pathUrl);
    // }

    // @GetMapping("/check-site")
    // public ResponseEntity<FetchResponse> checkUrl2(@RequestParam(value = "url") String queryUrl) {
    //     return fetchUrl(queryUrl);
    // }

    // private ResponseEntity<FetchResponse> fetchUrl(String pathUrl) {
    //     try {
    //         ResponseEntity<String> response = restTemplate.getForEntity(pathUrl, String.class);
    //         return ResponseEntity.status(response.getStatusCode()).body(new FetchResponse(pathUrl, response.getStatusCode().value()));
    //     } catch (ResourceAccessException e) {
    //         return ResponseEntity.status(404).body(new FetchResponse(pathUrl, 404));
    //     }
    // }
}

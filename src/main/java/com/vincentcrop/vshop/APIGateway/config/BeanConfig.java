package com.vincentcrop.vshop.APIGateway.config;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.converter.HttpMessageConverter;
import com.google.gson.Gson;

@Configuration
public class BeanConfig 
{
    @Bean
    public HttpMessageConverters messageConverter(ObjectProvider<HttpMessageConverter<?>> converter)
    {
        return new HttpMessageConverters(converter.orderedStream().collect(Collectors.toList()));
    }    

    @Bean
    public Gson gson()
    {
        return new Gson();
    }

    @Bean
    public static RestTemplate restTemplate(@Value("${http.connection-timeout.ms}") int connectTimeout)
    {
        // RestTemplate restTemplate = restTemplateBuilder
        //                 .setConnectTimeout(Duration.ofMillis(connectTimeout))
        //                 .setReadTimeout(Duration.ofMillis(connectTimeout))
        //                 .build();
                        
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new RestTemplateErrorHandler());
        return restTemplate;
    }

    private static class RestTemplateErrorHandler implements ResponseErrorHandler
    {

        @Override
        public boolean hasError(ClientHttpResponse response) throws IOException {
            return response.getStatusCode().is4xxClientError() || response.getStatusCode().is5xxServerError();
        }

        @Override
        public void handleError(ClientHttpResponse response) throws IOException {
            var inputStream = response.getBody();

            ByteArrayOutputStream result = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            for(int length; (length = inputStream.read(buffer)) != -1;)
            {
                result.write(buffer, 0, length);
            }

            System.out.println(result.toString(StandardCharsets.UTF_8));
        }
    }
}

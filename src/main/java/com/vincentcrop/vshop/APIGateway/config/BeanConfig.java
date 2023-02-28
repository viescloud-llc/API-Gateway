package com.vincentcrop.vshop.APIGateway.config;

import java.util.stream.Collectors;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

@Configuration
public class BeanConfig 
{
    @Bean
    public HttpMessageConverters messageConverter(ObjectProvider<HttpMessageConverter<?>> converter)
    {
        return new HttpMessageConverters(converter.orderedStream().collect(Collectors.toList()));
    }    
}

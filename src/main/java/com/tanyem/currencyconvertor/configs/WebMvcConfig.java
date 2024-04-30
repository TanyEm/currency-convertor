package com.tanyem.currencyconvertor.configs;

import com.tanyem.currencyconvertor.interceptors.HTTPResponseInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private HTTPResponseInterceptor httpResponseInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpResponseInterceptor);
    }
}
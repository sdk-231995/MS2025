package com.order.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import com.order.retry.CustomRetryListener;

@Configuration
//@Configuration(enforceUniqueMethods = false)
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Timeout settings
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000); // 3 seconds
        factory.setReadTimeout(5000);    // 5 seconds

        RestTemplate restTemplate = new RestTemplate(factory);
        restTemplate.setErrorHandler(new CustomRestTemplateErrorHandler());
        return restTemplate;

    }
    @Bean
    public RetryTemplate retryTemplate(CustomRetryListener listener) {
        RetryTemplate template = new RetryTemplate();
        template.registerListener(listener);
        return template;
    }
    
    @Bean
    public RestTemplate customRestTemplate(RestTemplateBuilder builder) {
        return builder
                .errorHandler(new CustomResponseErrorHandler())
                .build();
    }

}

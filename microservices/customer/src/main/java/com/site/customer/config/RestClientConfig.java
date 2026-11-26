package com.site.customer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // RestClient pre-configured with the base URL of the Orders microservice
    @Bean
    public RestClient ordersRestClient(RestClient.Builder builder,
                                       @Value("${orders.service.url}") String ordersServiceUrl) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(5000);

        return builder
                .baseUrl(ordersServiceUrl)
                .requestFactory(factory)
                .build();
    }
}

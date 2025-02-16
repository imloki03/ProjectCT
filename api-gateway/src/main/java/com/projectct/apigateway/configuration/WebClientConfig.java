package com.projectct.apigateway.configuration;

import com.projectct.apigateway.repository.AuthClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class WebClientConfig {
    @Bean
    @LoadBalanced
    WebClient.Builder webClientBuilder() {
        return WebClient.builder();
    }

    @Bean
    AuthClient authClient(WebClient.Builder webClientBuilder) {
        WebClient webClient = webClientBuilder.baseUrl("lb://auth-service").build();

        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClient))
                .build();

        return httpServiceProxyFactory.createClient(AuthClient.class);
    }
}

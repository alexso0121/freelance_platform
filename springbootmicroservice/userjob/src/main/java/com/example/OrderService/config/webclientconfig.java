package com.example.OrderService.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;

@Configuration
public class webclientconfig {
    @Bean
    public WebClient webClient(){
        return WebClient.builder().build();
    }
    @Bean
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

    @Bean
    @LoadBalanced //must added when running the eureka server
    public WebClient.Builder loadBalancedWebClientBuilder(){
        return WebClient.builder();
    }
}

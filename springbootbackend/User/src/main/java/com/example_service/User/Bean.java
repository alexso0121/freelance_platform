package com.example_service.User;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Configuration
public class Bean {
    @org.springframework.context.annotation.Bean
@Primary
public com.fasterxml.jackson.databind.ObjectMapper scmsObjectMapper() {
    com.fasterxml.jackson.databind.ObjectMapper responseMapper = new com.fasterxml.jackson.databind.ObjectMapper();
    return responseMapper;
}
}

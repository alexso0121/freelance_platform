package com.springboot.sohinalex.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


@SpringBootApplication
@EnableDiscoveryClient
public class ChatroomApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(ChatroomApplication.class, args);
    }




}

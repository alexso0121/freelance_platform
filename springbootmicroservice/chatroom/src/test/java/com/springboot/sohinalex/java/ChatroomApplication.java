package com.springboot.sohinalex.java;

import static org.junit.Assert.assertTrue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ChatroomApplication {

    public static void main(String[] args) {

        SpringApplication.run(ChatroomApplication.class, args);
    }




}

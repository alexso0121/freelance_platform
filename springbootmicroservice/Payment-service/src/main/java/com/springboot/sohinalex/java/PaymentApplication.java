package com.springboot.sohinalex.java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;


@SpringBootApplication
//@EnableDiscoveryClient
public class PaymentApplication extends SpringBootServletInitializer {


    public static void main(String[] args) {
        SpringApplication.run(PaymentApplication.class, args);
    }




}

package com.springboot.sohinalex.java;

import com.springboot.sohinalex.java.config.RsaKeyProp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
//@EnableDiscoveryClient
@EnableConfigurationProperties(RsaKeyProp.class)
public class ApiGatewayApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ApiGatewayApplication.class,args);
    }
}

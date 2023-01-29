package com.springboot.sohinalex.java.config;

import com.springboot.sohinalex.java.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.logging.Logger;

@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {



    @Autowired
    private JwtDecoder jwtDecoder;

    private final WebClient.Builder webClientBuilder;

    public AuthFilter(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            // Pre-processing
            if (config.isPreLogger()) {
                log.info("Pre GatewayFilter logging: "
                        + config.getBaseMessage());
            }

            if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                throw new RuntimeException("Missing auth information");}
            String authHeader=exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String[] parts=authHeader.split(" ");
            //get the bear token
            if(parts.length!=2|| !"Bearer".equals(parts[0])) {
                throw new RuntimeException(("Incorrect auth Structure"));
            }
            try{
            Jwt jwt = jwtDecoder.decode(parts[1]);
                if(jwt.getIssuedAt().toString()!="http://localhost:8080"){
                    throw new RuntimeException()
                }
            }catch (Exception exception){
                log.info("not a token");
            }



            return chain.filter(exchange);


        };


}
    public static class Config {
        private String baseMessage;
        private boolean preLogger;

        public boolean isPreLogger() {
            return preLogger;
        }

        public boolean isPostLogger() {
            return postLogger;
        }

        public void setPostLogger(boolean postLogger) {
            this.postLogger = postLogger;
        }

        public void setPreLogger(boolean preLogger) {
            this.preLogger = preLogger;
        }

        private boolean postLogger;

        public String getBaseMessage() {
            return baseMessage;
        }

        public void setBaseMessage(String baseMessage) {
            this.baseMessage = baseMessage;
        }

        // contructors, getters and setters...
    }
}

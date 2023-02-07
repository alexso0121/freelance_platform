package com.springboot.sohinalex.java.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
//add a filter to filter the non-valid jwt
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config>  {


    @Autowired
    private JwtDecoder jwtDecoder;




    public AuthFilter() {
        super(Config.class);

    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            log.info("filter by the Auth filter");
            // Pre-processing
            if (config.isPreLogger()) {
                log.info("Pre GatewayFilter logging: "
                        + config.getBaseMessage());
            }

            //check if it is bearer token
            if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                throw new RuntimeException("Missing auth information");}
            String authHeader=exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String[] parts=authHeader.split(" ");
            //get the bear token
            if(parts.length!=2|| !"Bearer".equals(parts[0])) {
                throw new RuntimeException(("Incorrect auth Structure"));
            }

            //validate the token
            Jwt jwt = jwtDecoder.decode(parts[1]);
                if(!jwt.getIssuer().toString().equals("http://localhost:8080") ||  !jwt.getClaim("secret").equals("AMDM:LM:LSMLdfsf") ){
                    System.out.println(jwt.getIssuer().toString());
                    throw new RuntimeException("Invalid token");
                }
                if(Objects.equals(jwt.getSubject(), "al")){
                    throw new RuntimeException("test valid");
                }

                //add headers with userid(for verification)

               // exchange.getRequest().getHeaders().add("id",jwt.getId() );
                log.info("user id is added to the header");


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

package com.springboot.sohinalex.java.service;

import com.springboot.sohinalex.java.Model.user_info;
import com.springboot.sohinalex.java.dto.AuthResponse;
import com.springboot.sohinalex.java.dto.NoticeRespond;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    private final WebClient.Builder webclient;

    @Autowired
    private  KafkaTemplate<String, NoticeRespond> kafkaTemplate;



    private final PasswordEncoder passwordEncoder;

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;


    private final WebClient.Builder webClientBuilder;



    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, WebClient.Builder webclient, PasswordEncoder passwordEncoder, ReactiveAuthenticationManager reactiveAuthenticationManager, WebClient.Builder webClientBuilder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.webclient = webclient;
        this.passwordEncoder = passwordEncoder;
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;

        this.webClientBuilder = webClientBuilder;
    }
    public String generateToken(Authentication user,int userid){
        log.info("generate token");

        Instant now=Instant.now();
        String scope=user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        String secret="AMDM:LM:LSMLdfsf";




        JwtClaimsSet claims=JwtClaimsSet.builder()
                .issuer("http://localhost:8080")
                .issuedAt(now)
                .expiresAt(now.plus(2, ChronoUnit.HOURS))
                .subject(user.getName())
                .id(String.valueOf(userid))
                .claim("scope",scope)
                .claim("secret",secret)

                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    }
    private Mono<Boolean> IsUsernameExist(String username){
        return webClientBuilder.baseUrl("http://USERJOB").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("UserJob/get/Byusername/{username}")//"http://localhost:8082/Checkuser/{id}")
                        .build(username))
                .retrieve()
                .bodyToMono(user_info.class)
                .doOnNext(res-> System.out.print(" afdasd"))
                .doOnError(res->{
                    log.info("good");
                    Mono.just(false);
                })
                .map(res->true
                );

    }



        public Mono<AuthResponse> signup(user_info user) {

            log.info("signup start");
            // convert IsUserexist to boolean

            Mono<Boolean> isuserexist=IsUsernameExist(user.getUsername())
                    .doOnNext(System.out::println)
                    .switchIfEmpty(Mono.just(false))  //no user found => can register
                    .mapNotNull(res-> res
                    );
            return isuserexist
                    .switchIfEmpty(Mono.error(new Error("cant check username exist")))
                    .doOnNext(System.out::println)
                    . flatMap(
                    nameExist->{   //check username exist
                        if(nameExist){
                            log.info("repeated");
                            return Mono.empty();
                        } else
                        {
                            log.info("not repeat");
                            user.setPassword(passwordEncoder.encode(user.getPassword()));    //encode the password
                            return webClientBuilder.baseUrl("http://USERJOB").build().post()
                                    .uri("UserJob/add/user")
                                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                                    .body(Mono.just(user), user_info.class)

                                    .retrieve()
                                    .bodyToMono(user_info.class)       //  /add/user
                                    .switchIfEmpty(Mono.error(new Error("cant save user")))
                                    .doOnNext(System.out::println)
                                    .flatMap(
                                    savedUser->{
                                        log.info("user saved with id: "+savedUser.getId());
                                    return   reactiveAuthenticationManager.authenticate(
                                                new UsernamePasswordAuthenticationToken(
                                                        user.getUsername(),user.getPassword()
                                                )
                                        ). switchIfEmpty(Mono.error(new Error("cant auth")))
                                            .doOnNext(System.out::println)
                                            .flatMap(       //generate token
                                                auth->{     //return id and token
                                            String token=generateToken(auth,savedUser.getId());
                                            log.info("token generated");
                                                    //send signup notification
                                                    LocalDateTime myDateObj = LocalDateTime.now();
                                                    DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                                                    String formattedDate = myDateObj.format(myFormatObj);
                                                    String notification="Welcome "+savedUser.getUsername()+"! You have signed up at "+formattedDate;
                                                    kafkaTemplate.send("notificationTopic",new NoticeRespond(
                                                            savedUser.getId(),formattedDate,notification
                                                    ));
                                            return Mono.just(new AuthResponse(savedUser.getId(), token));
                                    });
                                    }
                            );
                        }
                    }
            );

        }



    public Jwt getName(String token) {
        return jwtDecoder.decode(token);
    }


    public Mono<AuthResponse> signin(Authentication auth) {
        //auth
        return webClientBuilder.baseUrl("http://USERJOB").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/UserJob/get/Byusername/{username}")//"http://localhost:8082/Checkuser/{id}")
                        .build(auth.getName()))
                .retrieve()
                .bodyToMono(user_info.class)
                .doOnNext(user_info -> System.out.println(user_info.getPassword()))
                .switchIfEmpty(Mono.error(new BadCredentialsException("Username not found") ))
                .map(res->{
                    if(passwordEncoder.matches(auth.getCredentials().toString(),
                            res.getPassword())){
                        log.info("Password Match");

                        //send signin notice
                        LocalDateTime myDateObj = LocalDateTime.now();
                        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                        String formattedDate = myDateObj.format(myFormatObj);
                        String notification="Welcome "+res.getUsername()+"! You have signed in at "+formattedDate;
                        kafkaTemplate.send("notificationTopic",new NoticeRespond(
                                res.getId(),formattedDate,notification
                        ));

                        return new AuthResponse(res.getId(),generateToken(auth,res.getId()));
                    }
                    else{
                        log.info("invalid password");
                        return null;
                    }
                });



    }
}

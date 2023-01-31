package com.springboot.sohinalex.java.service;

import com.springboot.sohinalex.java.Model.User;
import com.springboot.sohinalex.java.dto.BasicAuth;
import com.springboot.sohinalex.java.dto.SignupDto;
import com.springboot.sohinalex.java.dto.UserAuthdto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;


import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    private final WebClient.Builder webclient;


    private final PasswordEncoder passwordEncoder;

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;



    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, WebClient.Builder webclient, PasswordEncoder passwordEncoder, ReactiveAuthenticationManager reactiveAuthenticationManager) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.webclient = webclient;
        this.passwordEncoder = passwordEncoder;
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;

    }
    public String generateToken(BasicAuth user){
        Instant now=Instant.now();
        /*String scope=authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));*/
        String secret="AMDM:LM:LSMLdfsf";

        JwtClaimsSet claims=JwtClaimsSet.builder()
                .issuer("http://localhost:8080")
                .issuedAt(now)
                .expiresAt(now.plus(2, ChronoUnit.HOURS))
                .subject(user.getUsername())
                .id(String.valueOf(user.getId()))
                //.claim("scope",scope)
                .claim("secret",secret)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    }

    public String signup(SignupDto user) throws Exception {
        Mono<UserAuthdto> saveduser=webclient.baseUrl("http://USER").build().post()
                .uri("/User/adduser")
                .header(MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(
                        new User(
                                0, user.getUsername(),
                                passwordEncoder.encode(user.getPassword()),
                                        user.getFullName(),
                                        user.getEmail(),
                                        user.getSkill_set(),user.getContact(),user.getCv(),user.getAddress_id()
                                ,user.getAddress(),null,3
                                        )
                ),User.class)
                .retrieve()
                .bodyToMono(User.class)
                .map(res->new UserAuthdto(res.getId(), res.getUsername(),null,
                        null))
                ;

        //
        Disposable subscribe = saveduser.subscribe(System.out::println);
        System.out.println(subscribe);
        System.out.println(saveduser);
                 //make syn request

        Mono<Authentication> authentication=  reactiveAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername()
                ,user.getPassword())
        );
        int id=getId(saveduser.map(res->res.getId()));

       if(id==0||saveduser.equals(Mono.empty())){
           log.info("Sign up error");
           return "Invalid";
       }

       log.info("successfully signup");
         return generateToken(new BasicAuth(id,user.getUsername(),user.getPassword()));

        }


        public Integer getId(Mono<Integer> Monoint) throws Exception{
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            PrintStream ps=new PrintStream(baos);
            PrintStream old=System.out;
            System.setOut(ps);
            System.out.println(Monoint);
        Monoint.subscribe(System.out::println);
        System.out.flush();
        System.setOut(old);
        return Integer.valueOf(baos.toString());
        }
    public Jwt getName(String token) throws Exception{
       Jwt jwt= jwtDecoder.decode(token);
      return jwt;
    }
}

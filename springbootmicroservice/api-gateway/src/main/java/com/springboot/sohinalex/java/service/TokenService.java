package com.springboot.sohinalex.java.service;

import com.springboot.sohinalex.java.Model.user_info;
import com.springboot.sohinalex.java.dto.SignupDto;
import com.springboot.sohinalex.java.respository.UserRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.springframework.security.core.GrantedAuthority;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    private final WebClient.Builder webclient;


    private final PasswordEncoder passwordEncoder;

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;

    private final UserRespository respository;



    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, WebClient.Builder webclient, PasswordEncoder passwordEncoder, ReactiveAuthenticationManager reactiveAuthenticationManager, UserRespository respository) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.webclient = webclient;
        this.passwordEncoder = passwordEncoder;
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;

        this.respository = respository;
    }
    public String generateToken(Authentication user){
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

                .claim("scope",scope)
                .claim("secret",secret)
                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    }

    public Mono<String> signup(SignupDto user) throws Exception {
        //check is repeated username or email
        if(respository.findByyUsername(user.getUsername()).equals(Mono.empty())
                ||respository.findByEmail(user.getEmail()).equals(Mono.empty())){
            log.info("repeated");
            throw new Exception("Repeated Username or email");
        }
        //save the user
         Mono<user_info> savedusr=respository.save( new user_info(0, user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                user.getFullName(),
                user.getEmail(),
                user.getSkill_set(),user.getContact(),user.getCv(),user.getAddress_id()
                ,user.getAddress(),null,3));
                log.info("generating token");
                //error
                if (savedusr.equals(Mono.empty())){
                    log.info("error occur");
                    throw new BadCredentialsException("error occur");}

                //get the user_id
                Mono<Integer> user_id=savedusr.map(user_info::getId)   ;

                log.info("login success with user_id: "+user_id);
                return reactiveAuthenticationManager.authenticate(
                                            new UsernamePasswordAuthenticationToken(
                                                    user.getUsername(),user.getPassword()
                                            )
                                    ).map(this::generateToken
                                    )
                                    ;








       /*return webclient.baseUrl("http://USER").build().post()
                .uri("/User/adduser")
                .header(MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(
                        new user_info(
                                0, user.getUsername(),
                                passwordEncoder.encode(user.getPassword()),
                                        user.getFullName(),
                                        user.getEmail(),
                                        user.getSkill_set(),user.getContact(),user.getCv(),user.getAddress_id()
                                ,user.getAddress(),null,3
                                        )
                ), user_info.class)
                .retrieve()
                .bodyToMono(user_info.class)
                .map(result->{
                    System.out.println(result);
                    if(result.getId()!=0){
                        Mono<Authentication> auth=reactiveAuthenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(result.getUsername()
                                        ,result.getPassword())
                        );
                        log.info("successfully signup");
                        if(auth.equals(Mono.empty())){
                            return "invalid";
                        }
                        //return generateToken(new Authentication() {
                                             }



                    log.info("Sign up error");
                    return "Invalid";

                }
                )
                ;

        //


                 //make syn request


       *//* int id=getId(saveduser.map(UserAuthdto::getId));*//*

*/




        /*public Integer getId(Mono<Integer> Monoint) throws Exception{
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            PrintStream ps=new PrintStream(baos);
            PrintStream old=System.out;
            System.setOut(ps);
            System.out.println(Monoint);
        Monoint.subscribe(System.out::println);
        System.out.flush();
        System.setOut(old);
        return Integer.valueOf(baos.toString());
        }*/

};

    }

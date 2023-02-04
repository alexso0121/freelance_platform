package com.springboot.sohinalex.java.service;

import com.springboot.sohinalex.java.Model.user_info;
import com.springboot.sohinalex.java.dto.AuthResponse;
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
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import org.springframework.security.core.GrantedAuthority;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
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

         Mono<user_info> MonoId =respository.findByyUsername(user.getName());



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
    private Mono<Boolean> IsUsernameExist(String username){
        return respository.findByyUsername(username)
                .doOnNext(res-> System.out.print(" afdasd"))
                .doOnError(res->{log.info("good");
                    Mono.just(false);
                })
                .map(res->true
                );

    }



        public Mono<String> signup(user_info user)  {

            log.info("signup start");
            return Mono.just(user).doOnNext(System.out::println)
                    .switchIfEmpty(Mono.error(new RuntimeException()))
                         .flatMap(Monouser->{
                     Mono<Boolean> isuserexist=IsUsernameExist(Monouser.getUsername())
                             .doOnNext(System.out::println)
                             .switchIfEmpty(Mono.just(false))  //no user found => can register
                             .mapNotNull(res-> res
                             );
                     Monouser.setPassword(  //encode the password
                                     passwordEncoder.encode(user.getPassword()));
                     Mono<user_info> savedusr = respository.save(
                     Monouser).log();

                            return isuserexist.doOnNext(System.out::println) //check the username exist in my db
                                    .switchIfEmpty(Mono.error(new RuntimeException()))
                                    .flatMap(
                                    res->{
                                        log.info("start the map");
                                        if(res){
                                        log.info("error");
                                        return null;
                                    }
                                    else {
                                        savedusr.subscribe();  //execute the saving user
                                        log.info("check");
                                        return reactiveAuthenticationManager.authenticate(
                                                new UsernamePasswordAuthenticationToken(
                                                        user.getUsername(),user.getPassword()
                                                )
                                        ).map(this::generateToken)


                                                ;
                                    }
                                    }
                            );

                          });}

               // return new AuthResponse(user_id.map(Integer::intValue),token.map(String::toString));






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



    public Jwt getName(String token) {
        return jwtDecoder.decode(token);
    }


    public Mono<String> signin(Authentication auth) {
        //auth
        return respository.findByyUsername(auth.getName())
                .doOnNext(user_info -> System.out.println(user_info.getPassword()))
                .switchIfEmpty(Mono.error(new BadCredentialsException("Username not found") ))
                .map(res->{
                    if(passwordEncoder.matches(auth.getCredentials().toString(),
                            res.getPassword())){
                        log.info("Password Match");
                        return
                                generateToken(auth);
                    }
                    else{
                        log.info("invalid password");
                        return null;
                    }
                });



    }
}

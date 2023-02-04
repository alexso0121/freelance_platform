package com.springboot.sohinalex.java.config;

import com.springboot.sohinalex.java.service.SecurityUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Configuration
@Slf4j
public class ReactiveAuthManger implements ReactiveAuthenticationManager {
    private final SecurityUserService securityUserService;
    private final PasswordEncoder passwordEncoder;

    public ReactiveAuthManger(SecurityUserService securityUserService, PasswordEncoder passwordEncoder) {
        this.securityUserService = securityUserService;
        this.passwordEncoder = passwordEncoder;

    }
//'$2a$10$71w.QlPp9NHGaJ7Q1tySO./agg5BaQc6l6lZHV.S.c5zgewSby3da'



    @Override
    public Mono<Authentication> authenticate(Authentication authentication) throws TypeMismatchException {
        log.info("Received authentication request");

       /* Mono<String> password=securityUserService.findByUsername(authentication.getName())
                .doOnNext(System.out::println)
                .map(UserDetails::getPassword);*/

        return Mono.just(authentication);
                /*.doOnNext(authentication1 -> System.out.println("start"))
                .switchIfEmpty(Mono.error(new RuntimeException()))
                    .map(auth->{
                        System.out.println(passwordEncoder.encode(auth.getCredentials().toString()));
                        //if(Mono.just(passwordEncoder.matches(auth.getCredentials().toString(),password.doOnNext(System.out::println).subscribe()))){

                                password.map(
                                res -> {
                            log.info("STart match");
                            if(passwordEncoder.matches(auth.getCredentials().toString(), res)){
                        log.info("password match");
                         return new UsernamePasswordAuthenticationToken(
                                 auth.getName(),null,auth.getAuthorities()
                         );

                    }else {
                            throw new BadCredentialsException("Bad Credients");

                        }

                    });
                        return auth;
                    });*/
}}

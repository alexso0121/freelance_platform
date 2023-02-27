package com.springboot.sohinalex.java.service;

import com.springboot.sohinalex.java.Model.SecurityUser;
import com.springboot.sohinalex.java.Model.user_info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

//class for converting the user class to security user class for authentication users
@Service
@Slf4j
public class SecurityUserService implements ReactiveUserDetailsService {



    private final WebClient.Builder webClientBuilder;

    public SecurityUserService(WebClient.Builder webClientBuilder) {

        this.webClientBuilder = webClientBuilder;
    }


    @Override
    public Mono<UserDetails> findByUsername(String username) {

            log.info("get user");


        return  webClientBuilder.baseUrl("UserJob").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/UserJob/get/Byusername/{username}")//"http://localhost:8082/Checkuser/{id}")
                        .build(username))
                .retrieve()
                .bodyToMono(user_info.class)
                .doOnNext(System.out::println)
                .switchIfEmpty(Mono.error(new RuntimeException()))
                .map(
                      SecurityUser::new
              );







        //map the userresult into securityuser
       /* if(result!=null){
            return Mono.just(new SecurityUser(result));
        }
        //no userfound
        return null;*/
    }
}

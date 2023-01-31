package com.springboot.sohinalex.java.service;

import com.springboot.sohinalex.java.Model.SecurityUser;
import com.springboot.sohinalex.java.dto.UserAuthdto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class SecurityUserService implements ReactiveUserDetailsService {

    @Autowired
    private WebClient.Builder webClientBuilder;



    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return webClientBuilder.baseUrl("http://USER").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("/User/AuthUser/{username}")
                        .build(username))
                .retrieve()
                .bodyToMono(UserAuthdto.class)
                .map(
                        SecurityUser::new
                        //res->new SecurityUser(res)
                )




                ; //make syn request
        //map the userresult into securityuser
       /* if(result!=null){
            return Mono.just(new SecurityUser(result));
        }
        //no userfound
        return null;*/
    }
}

package com.springboot.sohinalex.java.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthdto extends Mono<UserAuthdto> {

    private int id;
    private String username;
    private String password;
    private String role;

    @Override
    public void subscribe(CoreSubscriber<? super UserAuthdto> coreSubscriber) {

    }

}

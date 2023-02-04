package com.springboot.sohinalex.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Data
public class AuthResponse {

    private Mono<Integer> user_id;
    private String token;


}

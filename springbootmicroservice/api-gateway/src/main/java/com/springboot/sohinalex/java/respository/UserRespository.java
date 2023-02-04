package com.springboot.sohinalex.java.respository;

import com.springboot.sohinalex.java.Model.user_info;


import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRespository extends ReactiveSortingRepository<user_info,Integer> {
    @Query(value = "SELECT * FROM user_info WHERE username = :username ;")
    Mono<user_info> findByyUsername(String username);

    @Query(value = "SELECT id FROM user_info WHERE username = :username ;")
    Mono<Integer> findIdByUsername(String username);

    @Query(value ="SELECT * FROM user_info WHERE email = :email ")
    Mono<user_info> findByEmail(String email);

    Mono<user_info> save(user_info user);
}

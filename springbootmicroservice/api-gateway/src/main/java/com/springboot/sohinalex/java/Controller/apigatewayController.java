package com.springboot.sohinalex.java.Controller;


import com.springboot.sohinalex.java.Model.user_info;
import com.springboot.sohinalex.java.config.ReactiveAuthManger;
import com.springboot.sohinalex.java.dto.AuthResponse;
import com.springboot.sohinalex.java.dto.signupResponse;
import com.springboot.sohinalex.java.respository.UserRespository;
import com.springboot.sohinalex.java.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@RestController
@Slf4j
public class apigatewayController  {
    private final TokenService tokenService;
    private final UserRespository userRespository;

    public apigatewayController(TokenService tokenService, UserRespository userRespository) {
        this.tokenService = tokenService;
        this.userRespository = userRespository;

    }
    @GetMapping("/decode/{token}")
    public Jwt decode(@PathVariable String token) throws Exception {
        return tokenService.getName(token);
    }

    @PostMapping("/signin")
    public Mono<AuthResponse> login(Authentication auth) {
        log.info("start auth");
        return tokenService.signin(auth);
    }


//    @PostMapping("/signup")
//    public Mono<AuthResponse> signup(@RequestBody user_info user) {
//        return tokenService.signup(user)
//                .doOnNext(System.out::println)
//                .switchIfEmpty(Mono.error(new Error("signup not work")))
//                .flatMap(token->{
//                    return userRespository.findByyUsername(user.getUsername())
//                            .doOnNext(System.out::println)
//                            .switchIfEmpty(Mono.error(new Error("finding fail")))
//                            .flatMap(userInfo ->
//                                    Mono.just(new AuthResponse(userInfo.getId(),token)));
//                });
//
//
//
//     }
@PostMapping("/signup")
public Mono<AuthResponse> signup(@RequestBody user_info user) {
    return tokenService.signup(user);



}


    @GetMapping("/login/oauth2/code/google")
    public String loginWithGoogle(){
        return "youhavelogin";
    }
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        System.out.println(Collections.singletonMap("name", principal.getAttribute("")));

        return Collections.singletonMap("name", principal.getAttribute("email"));
    }
    @GetMapping("/showall")
    public OAuth2User getall(@AuthenticationPrincipal OAuth2User principal){
    return principal;
    }


}

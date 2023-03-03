package com.springboot.sohinalex.java.Controller;


import com.springboot.sohinalex.java.Model.user_info;
import com.springboot.sohinalex.java.dto.AuthResponse;

import com.springboot.sohinalex.java.dto.JobResponse;
import com.springboot.sohinalex.java.dto.SignupDto;
import com.springboot.sohinalex.java.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;

@RestController
@Slf4j
public class apigatewayController  {
    private final TokenService tokenService;


    public apigatewayController(TokenService tokenService) {
        this.tokenService = tokenService;


    }

    @GetMapping("/test")
    public String Test(){
        log.info("test");
        return "test success";
    }
    @GetMapping("/home")
    public Flux<JobResponse> homePage(){
        return tokenService.getAllJobs();
    }

    @GetMapping("/decode/{token}")
    public Jwt decode(@PathVariable String token) throws Exception {
        return tokenService.getName(token);
    }

    @PostMapping("/signin")
    public Mono<ResponseEntity<AuthResponse>> login(Authentication auth) {
        log.info("start auth");
        return tokenService.signin(auth);
    }




    @PostMapping("/signup")
    public Mono<ResponseEntity<AuthResponse>> signup(@RequestBody SignupDto user) {
    return tokenService.signup(user);


}
    //google login
    @GetMapping("/google/login")
    public OAuth2User getall(@AuthenticationPrincipal OAuth2User principal){
        return principal;
    }



  /*  @GetMapping("/login/oauth2/code/google")
    public String loginWithGoogle(){
        return "youhavelogin";
    }
    @GetMapping("/user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User principal) {
        System.out.println(Collections.singletonMap("name", principal.getAttribute("")));

        return Collections.singletonMap("name", principal.getAttribute("email"));
    }

   */


}

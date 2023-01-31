package com.springboot.sohinalex.java.Controller;


import com.springboot.sohinalex.java.dto.BasicAuth;
import com.springboot.sohinalex.java.dto.SignupDto;
import com.springboot.sohinalex.java.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@Slf4j
public class apigatewayController  {
    private final TokenService tokenService;

    public apigatewayController(TokenService tokenService) {
        this.tokenService = tokenService;
    }
    @GetMapping("/decode/{token}")
    public Jwt decode(@PathVariable String token) throws Exception {
        return tokenService.getName(token);
    }

    @PostMapping("/signin")
    public String login(BasicAuth basicAuth){
        String token=tokenService.generateToken(basicAuth);
        log.info("a token is distributed");
        return token;
    }
    @PostMapping("/signup")
    public String signup(@RequestBody SignupDto user) throws Exception {
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

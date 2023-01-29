package com.springboot.sohinalex.java.Controller;

import com.google.api.client.auth.openidconnect.IdTokenVerifier;
import com.springboot.sohinalex.java.service.TokenService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class apigatewayController extends IdTokenVerifier {
    private final TokenService tokenService;

    public apigatewayController(TokenService tokenService) {
        this.tokenService = tokenService;
    }
    @GetMapping("/decode/{token}")
    public Jwt decode(@PathVariable String token) throws Exception {
        return tokenService.getName(token);
    }

    @PostMapping("/signin")
    public String login(Authentication authentication){
        String token=tokenService.generateToken(authentication);
        return token;
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

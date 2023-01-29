package com.springboot.sohinalex.java.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Base64;

//setting the filter to allow bear auth
@Component
public class JwtFilter  implements WebFilter {

    @Autowired
    private JwtDecoder jwtDecoder;





    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authorizationheader= exchange.getRequest().getHeaders().get("Authorization").toString();
        String token;
        String Username = null;
        String iss=null;
        //check have tokem
        if(authorizationheader !=null&& authorizationheader.startsWith("Bearer ")){
            token=authorizationheader.substring(7);
            Username=jwtDecoder.decode(token).getSubject();
            iss= String.valueOf(jwtDecoder.decode(token).getIssuer());


        } //verify by check username and iss
        if(Username!=null && iss!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
            if(iss.equals("http://localhost:8080")){
                UserDetails userDetails=new User(Username,null,null);
                UsernamePasswordAuthenticationToken AuthenticationToken=new UsernamePasswordAuthenticationToken(
                        userDetails,null,userDetails.getAuthorities());
                //set username and id to the request

                SecurityContextHolder.getContext().setAuthentication(AuthenticationToken);
            }
        }
        return chain.filter(exchange);

    }}
 /* @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationheader=request.getHeader("Authorization");
        String token;
        String Username = null;
        String iss=null;
       //check have tokem
        if(authorizationheader !=null&& authorizationheader.startsWith("Bearer ")){
            token=authorizationheader.substring(7);
            Username=jwtDecoder.decode(token).getSubject();
            iss= String.valueOf(jwtDecoder.decode(token).getIssuer());


} //verify by check username and iss
        if(Username!=null && iss!=null&& SecurityContextHolder.getContext().getAuthentication()==null){
            if(iss.equals("http://localhost:8080")){
                UserDetails userDetails=new User(Username,null,null);
                UsernamePasswordAuthenticationToken AuthenticationToken=new UsernamePasswordAuthenticationToken(
                   userDetails,null,userDetails.getAuthorities());
                //set username and id to the request
                request.setAttribute("Username",Username);
                SecurityContextHolder.getContext().setAuthentication(AuthenticationToken);
            }
        }
        filterChain.doFilter(request,response);
    }*/

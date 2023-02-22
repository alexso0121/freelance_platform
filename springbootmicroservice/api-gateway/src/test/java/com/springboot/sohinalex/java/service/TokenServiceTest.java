package com.springboot.sohinalex.java.service;

import com.springboot.sohinalex.java.Model.user_info;
import com.springboot.sohinalex.java.dto.AuthResponse;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TokenServiceTest  {
    @InjectMocks
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;



    public void testGenerateToken() {

    }

    @Test
    public void testSignupSuccess() {

        TokenService Spy= spy(tokenService);
        user_info user=user_info.builder().id(1).username("alex").password("1234").build();

        doReturn(Mono.empty()).when(Spy).finduser("alex");  //no user with username found
        when(passwordEncoder.encode("1234")).thenReturn("encode1234");
        doReturn("token").when(Spy).generateToken(any(),eq(1));
        doNothing().when(Spy).sendNotice("Welcome alex! You have successfully signed up ",1);
        user_info encodeuser=user_info.builder().id(1).username("alex").password("encode1234").build();

        doReturn(Mono.just(new UsernamePasswordAuthenticationToken("alex","encode1234"))).when(Spy).BuildAuthentication(encodeuser);
        doReturn(Mono.just(user)).when(Spy).saveUser(user);

        Mono<AuthResponse> res=Spy.signup(user);
        StepVerifier
                .create(res)
                .consumeNextWith(
                        result->{

                            Assertions.assertEquals("token",result.getToken());
                            Assertions.assertEquals(1,result.getUser_id());
                            System.out.println("test pass");
                        }
                )

                .verifyComplete();




    }
    //Test if the username already existed in the database
    @Test
    public void testSignupFail() {

        TokenService Spy= spy(tokenService);
        user_info user=user_info.builder().id(1).username("alex").password("1234").build();

        doReturn(Mono.just(user)).when(Spy).finduser("alex"); //if username is already exist
        when(passwordEncoder.encode("1234")).thenReturn("encode1234");
        doReturn("token").when(Spy).generateToken(any(),eq(1));
        doNothing().when(Spy).sendNotice("Welcome alex! You have successfully signed up ",1);
        user_info encodeuser=user_info.builder().id(1).username("alex").password("encode1234").build();

        doReturn(Mono.just(new UsernamePasswordAuthenticationToken("alex","encode1234"))).when(Spy).BuildAuthentication(encodeuser);
        doReturn(Mono.just(user)).when(Spy).saveUser(user);

        Mono<AuthResponse> res=Spy.signup(user);
        StepVerifier
                .create(res)
                .expectError();


    }


    @Test
    public void testSigninSuccess() {
        String correctPw="1234";
        Authentication auth=new UsernamePasswordAuthenticationToken("alex",correctPw);
        TokenService Spy= spy(tokenService);
        user_info user=user_info.builder().id(1).username("alex").password(correctPw).build();

        doReturn(Mono.just(user)).when(Spy).finduser("alex");
        when(passwordEncoder.matches(correctPw,correctPw)).thenReturn(true);
        doNothing().when(Spy).sendNotice("Welcome alex! You have successfully signed in ",1);
        doReturn("token").when(Spy).generateToken(any(),eq(1));

        Mono<AuthResponse> res=Spy.signin(auth);

        StepVerifier
                .create(res)
                .consumeNextWith(
                        authResponse -> {
                            Assertions.assertEquals(authResponse.getToken(),"token");
                            Assertions.assertEquals(authResponse.getUser_id(),1);
                        }
                )
                .verifyComplete();

    }

    //test when password unmatched
    //not generate token
    @Test
    public void testSigninUnmatchedPassword() {
        String correctPw="1234";
        Authentication auth=new UsernamePasswordAuthenticationToken("alex","falsepassword");
        TokenService Spy= spy(tokenService);
        user_info user=user_info.builder().id(1).username("alex").password(correctPw).build();

        doReturn(Mono.just(user)).when(Spy).finduser("alex");
        when(passwordEncoder.matches(correctPw,correctPw)).thenReturn(true);
        doNothing().when(Spy).sendNotice("Welcome alex! You have successfully signed in ",1);
        doReturn("token").when(Spy).generateToken(any(),eq(1));

        Mono<AuthResponse> res=Spy.signin(auth);

        StepVerifier
                .create(res)
                .expectError();

    }
    @Test
    public void testSigninNoUserFound() {
        String correctPw="1234";
        Authentication auth=new UsernamePasswordAuthenticationToken("alex",correctPw);
        TokenService Spy= spy(tokenService);
        user_info user=user_info.builder().id(1).username("alex").password(correctPw).build();

        //no user found
        doReturn(Mono.empty()).when(Spy).finduser("alex");
        when(passwordEncoder.matches(correctPw,correctPw)).thenReturn(true);
        doNothing().when(Spy).sendNotice("Welcome alex! You have successfully signed in ",1);
        doReturn("token").when(Spy).generateToken(any(),eq(1));

        Mono<AuthResponse> res=Spy.signin(auth);

        StepVerifier
                .create(res)
                .expectError();

    }


}
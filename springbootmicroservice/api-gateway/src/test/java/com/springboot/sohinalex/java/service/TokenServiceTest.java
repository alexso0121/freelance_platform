package com.springboot.sohinalex.java.service;

import com.springboot.sohinalex.java.Model.user_info;
import com.springboot.sohinalex.java.dto.AuthResponse;
import com.springboot.sohinalex.java.dto.SignupDto;
import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class TokenServiceTest  {
    @InjectMocks
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    private final UUID uuid1=UUID.randomUUID();



    public void testGenerateToken() {

    }

    //test when signup with username not exist in the database
    @Test
    public void testSignupSuccess() {

        TokenService Spy= spy(tokenService);
        user_info user=user_info.builder().id(uuid1).username("alex").password("1234").build();
        SignupDto signupDto=SignupDto.builder().username("alex").password("1234").build();


        doReturn(Mono.empty()).when(Spy).finduser("alex");
       //no user with username found
        when(passwordEncoder.encode("1234")).thenReturn("encode1234");
        doReturn("token").when(Spy).generateToken(any(),eq(uuid1));

        doNothing().when(Spy).sendNotice("Welcome alex! You have successfully signed up ",uuid1);
        SignupDto encodeuser=SignupDto.builder().username("alex").password("encode1234").build();

        doReturn(Mono.just(new UsernamePasswordAuthenticationToken("alex","encode1234"))).when(Spy)
                .BuildAuthentication(tokenService.SignupDtoTouserInfo(encodeuser));
        doReturn(Mono.just(user)).when(Spy).saveUser(any());

        Mono<ResponseEntity<AuthResponse>> res=Spy.signup(signupDto);
        StepVerifier
                .create(res)
                .consumeNextWith(
                        result->{

                            Assertions.assertEquals("token", Objects.requireNonNull(result.getBody()).getToken());
                            Assertions.assertEquals(uuid1, Objects.requireNonNull(result.getBody()).getUser_id());
                            System.out.println("test pass");
                        }
                )

                .verifyComplete();




    }
    //Test if the username already existed in the database
    @Test
    public void testSignupFail() {

        TokenService Spy= spy(tokenService);
        user_info user=user_info.builder().id(uuid1).username("alex").password("1234").build();

        SignupDto signupDto=SignupDto.builder().username("alex").password("1234").build();


        doReturn(Mono.just(user)).when(Spy).finduser("alex"); //if username is already exist
        when(passwordEncoder.encode("1234")).thenReturn("encode1234");
        doReturn("token").when(Spy).generateToken(any(),eq(uuid1));
        doNothing().when(Spy).sendNotice("Welcome alex! You have successfully signed up ",uuid1);
        user_info encodeuser=user_info.builder().id(uuid1).username("alex").password("encode1234").build();

        doReturn(Mono.just(new UsernamePasswordAuthenticationToken("alex","encode1234"))).when(Spy).BuildAuthentication(encodeuser);
        doReturn(Mono.just(user)).when(Spy).saveUser(any());

        Mono<ResponseEntity<AuthResponse>> res=Spy.signup(signupDto);
        StepVerifier
                .create(res)
                .consumeNextWith(
                        result->{

                            Assertions.assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());

                            System.out.println("test pass");
                        }
                )

                .verifyComplete();


    }



    //test with match password
    @Test
    public void testSigninSuccess() {
        String correctPw="1234";
        Authentication auth=new UsernamePasswordAuthenticationToken("alex",correctPw);
        TokenService Spy= spy(tokenService);
        user_info user=user_info.builder().id(uuid1).username("alex").password(correctPw).build();

        doReturn(Mono.just(user)).when(Spy).finduser("alex");
        when(passwordEncoder.matches(correctPw,correctPw)).thenReturn(true);
        doNothing().when(Spy).sendNotice("Welcome alex! You have successfully signed in ",uuid1);
        doReturn("token").when(Spy).generateToken(any(),eq(uuid1));

        Mono<ResponseEntity<AuthResponse>> res=Spy.signin(auth);

        StepVerifier
                .create(res)
                .consumeNextWith(
                        authResponse -> {
                            Assertions.assertEquals("token", Objects.requireNonNull(authResponse.getBody()).getToken());
                            Assertions.assertEquals(uuid1,authResponse.getBody().getUser_id());
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
        user_info user=user_info.builder().id(uuid1).username("alex").password(correctPw).build();

        doReturn(Mono.just(user)).when(Spy).finduser("alex");
        when(passwordEncoder.matches(correctPw,correctPw)).thenReturn(true);
        doNothing().when(Spy).sendNotice("Welcome alex! You have successfully signed in ",uuid1);
        doReturn("token").when(Spy).generateToken(any(),eq(uuid1));

        Mono<ResponseEntity<AuthResponse>> res=Spy.signin(auth);

        StepVerifier
                .create(res)
                .consumeNextWith(
                        result->{

                            Assertions.assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());

                        }
                )

                .verifyComplete();

    }

    //test when the username not exist
    @Test
    public void testSigninNoUserFound() {
        String correctPw="1234";
        Authentication auth=new UsernamePasswordAuthenticationToken("alex",correctPw);
        TokenService Spy= spy(tokenService);
        user_info user=user_info.builder().id(uuid1).username("alex").password(correctPw).build();

        //no user found
        doReturn(Mono.empty()).when(Spy).finduser("alex");
        when(passwordEncoder.matches(correctPw,correctPw)).thenReturn(true);
        doNothing().when(Spy).sendNotice("Welcome alex! You have successfully signed in ",uuid1);
        doReturn("token").when(Spy).generateToken(any(),eq(uuid1));

        Mono<ResponseEntity<AuthResponse>> res=Spy.signin(auth);

        StepVerifier
                .create(res)
                .consumeNextWith(
                        result->{

                            Assertions.assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());

                        }
                )

                .verifyComplete();
    }


}
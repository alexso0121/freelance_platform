package com.springboot.sohinalex.java.service;

import com.springboot.sohinalex.java.Model.user_info;
import com.springboot.sohinalex.java.dto.AuthResponse;
import com.springboot.sohinalex.java.dto.NoticeRespond;
import com.springboot.sohinalex.java.dto.SignupDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TokenService {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    private final WebClient.Builder webclient;

    @Autowired
    private  KafkaTemplate<String, NoticeRespond> kafkaTemplate;



    private final PasswordEncoder passwordEncoder;

    private final ReactiveAuthenticationManager reactiveAuthenticationManager;


    private final WebClient.Builder webClientBuilder;



    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder, WebClient.Builder webclient, PasswordEncoder passwordEncoder, ReactiveAuthenticationManager reactiveAuthenticationManager, WebClient.Builder webClientBuilder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
        this.webclient = webclient;
        this.passwordEncoder = passwordEncoder;
        this.reactiveAuthenticationManager = reactiveAuthenticationManager;

        this.webClientBuilder = webClientBuilder;
    }

    //convert signupDto to user_info class
    public user_info SignupDtoTouserInfo(SignupDto signupDto){
        return user_info.builder()
                .username(signupDto.getUsername())
                .password(signupDto.getPassword())
                .fullName(signupDto.getFullName())
                .email(signupDto.getEmail())
                .skill_set(signupDto.getSkill_set())
                .contact(signupDto.getContact())
                .cv(signupDto.getCv())
                .Address_id(signupDto.getAddress_id()).build();


    }
    //convert for generating jwt token
    public String generateToken(Authentication user,int userid){
        log.info("generate token");

        Instant now=Instant.now();
        //set authorities
        String scope=user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        String secret="AMDM:LM:LSMLdfsf";




        JwtClaimsSet claims=JwtClaimsSet.builder()
                .issuer("http://localhost:8080")
                .issuedAt(now)
                .expiresAt(now.plus(3, ChronoUnit.HOURS))
                .subject(user.getName())
                .id(String.valueOf(userid))
                .claim("scope",scope)
                .claim("secret",secret)

                .build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    }


    //check if the username exist or not
    public Mono<Boolean> IsUsernameExist(String username){ //convert user to boolean
        return finduser(username)
                .doOnNext(System.out::print)
                .doOnError(res->{
                    log.info("good");
                    Mono.just(false);
                })
                .map(res->true
                );

    }
    //find the user from the database and userjob microservice
    public Mono<user_info> finduser(String username){
        return webClientBuilder.baseUrl("http://UserJob").build().get()
                .uri(uriBuilder -> uriBuilder
                        .path("UserJob/get/Byusername/{username}")//"http://localhost:8082/Checkuser/{id}")
                        .build(username))
                .retrieve()
                .bodyToMono(user_info.class);
    }

    //save the user_info to the database and userjob microservice
    public Mono<user_info> saveUser(user_info user){

        return webClientBuilder.baseUrl("http://UserJob").build().post()
                .uri("UserJob/add/user")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(user), user_info.class)

                .retrieve()
                .bodyToMono(user_info.class)       //  /add/user
                .switchIfEmpty(Mono.error(new Error("cant save user")))
                .doOnNext(System.out::println);
    }

    //build the authentication object
    public Mono<Authentication> BuildAuthentication(user_info user){
        return reactiveAuthenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(),user.getPassword()
                        )
                ). switchIfEmpty(Mono.error(new Error("cant auth")))
                .doOnNext(System.out::println);
    }

    //sending notification to the user when auth success
    public void sendNotice(String notification,int userid){
        LocalDateTime myDateObj = LocalDateTime.now();
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = myDateObj.format(myFormatObj);
        kafkaTemplate.send("notificationTopic",new NoticeRespond(
                userid,formattedDate,notification
        ));
    }

    //sign up
    public Mono<AuthResponse> signup(SignupDto user) {

            log.info("signup start");

            //convert Isusernameexist method to return either true or false
            Mono<Boolean> isuserexist=IsUsernameExist(user.getUsername())
                    .doOnNext(System.out::println)
                    .switchIfEmpty(Mono.just(false))  //no user found => can register
                    .mapNotNull(res-> res
                    );


            return isuserexist
                    .switchIfEmpty(Mono.error(new Error("cant check username exist")))
                    .doOnNext(System.out::println)
                    . flatMap(
                    nameExist->{   //check username exist
                        if(nameExist){  //case when the username already exist=>return error
                            log.info("repeated");
                            return Mono.error(new Error("username exist"));
                        } else      //case when no username found=> can signup
                        {
                            log.info("not repeat");
                            user.setPassword(passwordEncoder.encode(user.getPassword()));    //encode the password
                            return saveUser(SignupDtoTouserInfo(user))       //saving the user information
                                    .flatMap(
                                    savedUser->{
                                        log.info("user saved with id: "+savedUser.getId());
                                    return   BuildAuthentication(SignupDtoTouserInfo(user)  )   //converting user_info object to authentication obj
                                            .flatMap(
                                                auth->{
                                                    String token=generateToken(auth,savedUser.getId()); //generate token
                                                    log.info("token generated");
                                                    //send signup notification
                                                    String notification="Welcome "+savedUser.getUsername()+"! You have successfully signed up ";
                                                    sendNotice(notification,savedUser.getId());
                                                    return Mono.just(new AuthResponse(savedUser.getId(), token)); //return id and token
                                    });
                                    }
                            );
                        }
                    }
            );

        }



    public Jwt getName(String token) {
        return jwtDecoder.decode(token);
    }


    //sign in
    public Mono<AuthResponse> signin(Authentication auth) {
        //auth
        return finduser(auth.getName())     //get user credential from database
                .doOnNext(user_info -> System.out.println(user_info.getPassword()))
                .switchIfEmpty(Mono.error(new BadCredentialsException("Username not found") ))
                .map(res->{
                    if(passwordEncoder.matches(auth.getCredentials().toString(),    //auth check
                            res.getPassword())){
                        log.info("Password Match");

                        //send signin notice
                        String notification="Welcome "+res.getUsername()+"! You have successfully signed in ";
                        sendNotice(notification,res.getId());

                        return new AuthResponse(res.getId(),generateToken(auth,res.getId()));
                    }
                    else{
                        log.info("invalid password");
                        return null;
                    }
                });



    }
}

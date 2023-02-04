package com.springboot.sohinalex.java.config;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.springboot.sohinalex.java.service.SecurityUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;


import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
//@EnableWebSecurity
@EnableReactiveMethodSecurity
@EnableWebFluxSecurity

@Slf4j

public class SecurityConfig  {

    private final RsaKeyProp rsaKeys;

    private final SecurityUserService securityUserService;





    public SecurityConfig(RsaKeyProp rsaKeys, SecurityUserService securityUserService) {
        this.rsaKeys = rsaKeys;
        this.securityUserService = securityUserService;


    }
   /* @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)  {
        try{System.out.println(authConfig.getAuthenticationManager());
            return authConfig.getAuthenticationManager();}catch(Exception exception){
            System.out.println(exception);
            return null;
        }
    } */
   @Bean
   public PasswordEncoder passwordEncoder() {
       return new BCryptPasswordEncoder();
   }


   /* @Bean
    protected ReactiveAuthenticationManager reactiveAuthenticationManager() {

        log.info("Received authentication request");

        return authentication -> {


            UserDetailsRepositoryReactiveAuthenticationManager authenticator = new UserDetailsRepositoryReactiveAuthenticationManager(securityUserService);
            authenticator.setPasswordEncoder(passwordEncoder());

            return authenticator.authenticate(authentication);
        };
    }*/


    @Bean
    public SecurityWebFilterChain filterChain(ServerHttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                /*.csrf(csrf -> csrf.ignoringRequestMatchers("/Job/getRegionjobs/**",
                        "/Job/getalljobs","/login/oauth2/code/google"))*/
                .csrf(csrf -> csrf.disable())

                .authorizeExchange(auth->
                        auth.pathMatchers("/signup","/signin").permitAll()

                                .anyExchange().authenticated()
                       )

                // .addFilterAt(jwtFilter,SecurityWebFiltersOrder.LOGOUT)
                .oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt)
                //.sessionManagement(session-> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(withDefaults())
                //.addFilterAfter(new AuthenticationWebFilter(reactiveAuthManger), SecurityWebFiltersOrder.REACTOR_CONTEXT)
                .build();



    }
   /* @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        UserDetails user = User.withDefaultPasswordEncoder()
                .username("dfsdfd")
                .password("user")
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService(user);
    }*/
    //to decode and encode are based on the keys
    @Bean
    JwtEncoder jwtEncoder(){
        JWK jwk=new RSAKey.Builder(rsaKeys.publicKey()).privateKey(rsaKeys.privateKey()).build();
        JWKSource<SecurityContext> jwks =new ImmutableJWKSet<>(new JWKSet((jwk)));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    JwtDecoder jwtDecoder(){
        return NimbusJwtDecoder.withPublicKey(rsaKeys.publicKey()).build();
    }




}

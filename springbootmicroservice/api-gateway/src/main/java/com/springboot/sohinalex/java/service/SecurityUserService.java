package com.springboot.sohinalex.java.service;

import com.springboot.sohinalex.java.Model.SecurityUser;
import com.springboot.sohinalex.java.Model.user_info;
import com.springboot.sohinalex.java.respository.UserRespository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class SecurityUserService implements ReactiveUserDetailsService {


    private final UserRespository userRespository;

    public SecurityUserService(UserRespository userRespository) {
        this.userRespository = userRespository;
    }


    @Override
    public Mono<UserDetails> findByUsername(String username) {

            log.info("get user");

        Mono<UserDetails> ans=  userRespository.findByyUsername(username)
                      .switchIfEmpty(Mono.error(new RuntimeException())).map(
                      SecurityUser::new
              );

        ans.doOnNext(System.out::println).subscribe();

              return ans;





        //map the userresult into securityuser
       /* if(result!=null){
            return Mono.just(new SecurityUser(result));
        }
        //no userfound
        return null;*/
    }
}

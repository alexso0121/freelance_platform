package com.example_service.User.Service;

import com.example_service.User.Model.User;
import com.example_service.User.Repository.UserRespository;
import com.example_service.User.dto.UserAuthdto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.CoreSubscriber;
import reactor.core.publisher.Mono;

@Service
public class UserCoreService {
    @Autowired
    private UserRespository userRespository;
    public Boolean VerifyCanOrder(int id){
        User user=userRespository.findById(id).orElse(null);
        if(user.getScore()<2.0){
            return false;
        }
        return true;

    };

    public UserAuthdto getSecurityUser(String username){
        User existuser=userRespository.findUserbyName(username);
        if(existuser==null){
            return null;
        }
        return new UserAuthdto(existuser.getId()
                , existuser.getUsername()
                , existuser.getPassword()
                , existuser.getRole()) ;

            
    }
}

package com.example_service.User.Service;

import com.example_service.User.Model.User;
import com.example_service.User.Repository.UserRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}

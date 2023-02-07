package com.example.OrderService.Service;


import com.example.OrderService.Entity.User;
import com.example.OrderService.Repository.UserRepository;
import com.example.OrderService.dto.AuthResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.EmptyStackException;
import java.util.Objects;

@Service
public class UserCrudservice {
    @Autowired
    private UserRepository userRespository;

   
    public AuthResponse signup(User user){
        AuthResponse res=new AuthResponse();

         if(getUserbyName(user.getUsername())!=null){
             res.setToken("the username has already been used");
             return res;
         }else if(userRespository.findUserbyEmail(user.getEmail())!=null){
             res.setToken("the email has already been registered");
         }else{
             User saveduser=userRespository.save(user);
             res.setId(saveduser.getId());
             res.setToken("Successful signup");
         }
         return res;
    }

    public User getUserbyName(String name){

        return userRespository.findUserbyName(name);
    }


    public String Delete(int id){
        userRespository.deleteById(id);
        return "User with id "+id +"is removed";
    }



}

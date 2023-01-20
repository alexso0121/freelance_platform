package com.example_service.User.Service;

import com.example_service.User.Model.User;
import com.example_service.User.Repository.UserRespository;
import com.example_service.User.dto.signupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.EmptyStackException;
import java.util.Objects;

@Service
public class Userservice {
    @Autowired
    private UserRespository userRespository;

   
    public signupResponse signup(User user){
        signupResponse res=new signupResponse();

         if(getUserbyName(user.getName())!=null){
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

    public String updateUser(User user){
        User existinguser=userRespository.findById(user.getId()).orElseThrow(EmptyStackException::new);
        if(!Objects.equals(existinguser.getName(), existinguser.getName()) &&getUserbyName(user.getName())!=null){
            return "the username has already been used";
        }else if(!Objects.equals(existinguser.getEmail(), existinguser.getEmail()) &&userRespository.findUserbyEmail(user.getEmail())!=null){
            return "the email has already been registered";
        }else{
        existinguser.setCv(user.getCv());
        existinguser.setEmail(user.getEmail());
        existinguser.setAddress(user.getAddress());
        existinguser.setAddress_id(user.getAddress_id());
        existinguser.setFullName(user.getFullName());
        existinguser.setSkillSet(user.getSkillSet());
        existinguser.setContact(user.getContact());
        existinguser.setName(user.getName());
        userRespository.save(existinguser);
        return "Successful update";}
    }
    public String Delete(int id){
        userRespository.deleteById(id);
        return "User with id "+id +"is removed";
    }
}

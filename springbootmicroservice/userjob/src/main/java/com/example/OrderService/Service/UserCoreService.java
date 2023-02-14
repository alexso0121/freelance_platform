package com.example.OrderService.Service;


import com.example.OrderService.Entity.User;
import com.example.OrderService.Repository.UserRepository;
import com.example.OrderService.dto.InfoResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@Slf4j
public class UserCoreService {

    private final UserRepository userRespository;


    public UserCoreService(UserRepository userRespository) {
        this.userRespository = userRespository;

    }

    public Boolean VerifyCanOrder(int id){
        User user=userRespository.findById(id).orElse(null);
        if(user.getScore()<2.0){
            return false;
        }
        return true;

    };


    public InfoResponse getProfile( int id){
        return UserToDTO(userRespository
                        .findById(id).orElse(null));

    }
    public String updateUser(User user){
        User existinguser=userRespository.findById(user.getId()).orElseThrow(Error::new);
        if(!Objects.equals(user.getUsername(), existinguser.getUsername()) &&findByUsername(user.getUsername())!=null){
            return "the username has already been used";
        }else if(!Objects.equals(user.getEmail(), existinguser.getEmail()) &&userRespository.findUserbyEmail(user.getEmail())!=null){
            return "the email has already been registered";
        }else{
            existinguser.setCv(user.getCv());
            existinguser.setEmail(user.getEmail());
            existinguser.setAddress(user.getAddress());
            existinguser.setAddress_id(user.getAddress_id());
            existinguser.setFullName(user.getFullName());
            existinguser.setSkill_set((user.getSkill_set()));
            existinguser.setContact(user.getContact());
            existinguser.setUsername(user.getUsername());
             saveAndReturn(existinguser);
            return "Successful update";}
    }


    public User getUser(int id){
        return userRespository.findById(id).orElse(null);
    }
    private InfoResponse UserToDTO(User user){
        if(user==null){return null;}
        InfoResponse respond=new InfoResponse();
        respond.setId(user.getId());
        respond.setUsername(user.getUsername());
        respond.setAddress(user.getAddress());
        respond.setCv(user.getCv());
        respond.setContact(user.getContact());
        respond.setEmail(user.getEmail());
        respond.setFullName(user.getFullName());
        respond.setSkillSet(user.getSkill_set());
        return respond;
    }

    public User findById(int id) {
        return userRespository.findById(id).orElse(null);
    }


    public User findByUsername(String username) {
        return userRespository.findUserbyName(username);
    }

    public User saveAndReturn(User user) {

        return userRespository.save(user);
    }
    //update application list in user

    public String Delete(int id){
        userRespository.deleteById(id);
        return "User with id "+id +"is removed";
    }

}
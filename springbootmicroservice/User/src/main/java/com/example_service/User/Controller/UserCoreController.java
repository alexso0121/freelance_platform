package com.example_service.User.Controller;

import com.example_service.User.Model.User;
import com.example_service.User.Repository.UserRespository;
import com.example_service.User.Service.UserCoreService;
import com.example_service.User.Service.UserCrudservice;
import com.example_service.User.dto.AuthResponse;
import com.example_service.User.dto.InfoResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
public class UserCoreController {
    @Autowired
    private UserCoreService userCoreservice;
    @Autowired
    private UserRespository userRespository;

    @GetMapping("/Checkuser/{id}")
    public Boolean VerifyCanOrder(@PathVariable int id){
        return userCoreservice.VerifyCanOrder(id);
    }

    @GetMapping("/getProfile/{id}")
    public InfoResponse getProfile(@PathVariable int id){
        return UserToDTO(Objects.requireNonNull(userRespository.findById(id).orElse(null)));

    }
    public AuthResponse login(){

        return null;
    };
    private InfoResponse UserToDTO(User user){
        InfoResponse respond=new InfoResponse();
        respond.setId(user.getId());
        respond.setName(user.getName());
        respond.setAddress(user.getAddress());
        respond.setCv(user.getCv());
        respond.setContact(user.getContact());
        respond.setEmail(user.getEmail());
        respond.setFullName(user.getFullName());
        respond.setSkillSet(user.getSkill_set());
    return respond;
    }


}
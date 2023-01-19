package com.example_service.User.Controller;

import com.example_service.User.Model.User;
import com.example_service.User.Repository.UserRespository;
import com.example_service.User.Service.Userservice;
import com.example_service.User.dto.signupResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.EmptyStackException;

@RestController
public class UserContoller {
    @Autowired
    private Userservice userservice;

    @Autowired
    private UserRespository userRespository;

    @GetMapping
    public User getUserbyid(@PathVariable int id){
        return userRespository.findById(id).orElse(null);

    }

    //should be change to token later on
    @PostMapping
    public signupResponse signup(@RequestBody User user){
        return userservice.signup(user);
    }




}
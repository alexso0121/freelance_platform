package com.example_service.User.Controller;

import com.example_service.User.Model.User;
import com.example_service.User.Repository.UserRespository;
import com.example_service.User.Service.Userservice;
import com.example_service.User.dto.signupResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.EmptyStackException;

@RestController
public class UserContoller {

    private static final Logger LOG = LoggerFactory.getLogger(UserContoller.class);
    @Autowired
    private Userservice userservice;



    @Autowired
    private UserRespository userRespository;

    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public User getUserbyid(@PathVariable int id){
        return userRespository.findById(id).orElse(null);

    }

    //should be change to token later on
    @PostMapping("/adduser")
    @ResponseStatus(HttpStatus.CREATED)
    public signupResponse signup(@RequestBody User user){
        LOG.info("Token requested for user: '{}",user.getName());
        return userservice.signup(user);
    }

    //the user id  be  set in frontend
    @PutMapping("/updateuser")
    public String UpdateUser (@RequestBody User user){
        LOG.info("Updated user: '{}",user.getId());
        return userservice.updateUser(user);
    }

    @DeleteMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable int id){
        LOG.info("Deleted user id: '{}",id);
        return userservice.Delete(id);
    }



}
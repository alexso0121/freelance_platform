package com.example_service.User.Controller;

import com.example_service.User.Model.User;
import com.example_service.User.Repository.UserRespository;
import com.example_service.User.Service.UserCoreService;
import com.example_service.User.Service.UserCrudservice;
import com.example_service.User.dto.AuthResponse;
import com.example_service.User.dto.InfoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserCrudContoller {

    private static final Logger LOG = LoggerFactory.getLogger(UserCrudContoller.class);
    @Autowired
    private UserCrudservice userCrudservice;



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
    public AuthResponse signup(@RequestBody User user){
        LOG.info("Token requested for user: '{}",user.getName());
        return userCrudservice.signup(user);
    }




    //the user id  be  set in frontend
    @PutMapping("/updateuser")
    public String UpdateUser (@RequestBody User user){
        LOG.info("Updated user: '{}",user.getId());
        return userCrudservice.updateUser(user);
    }

    @DeleteMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable int id){
        LOG.info("Deleted user id: '{}",id);
        return userCrudservice.Delete(id);
    }







}
package com.example.OrderService.Controller;


import com.example.OrderService.Entity.User;
import com.example.OrderService.Repository.UserRepository;
import com.example.OrderService.Service.UserCrudservice;
import com.example.OrderService.dto.AuthResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/User")
public class UserCrudContoller {

    private static final Logger LOG = LoggerFactory.getLogger(UserCrudContoller.class);
    @Autowired
    private UserCrudservice userCrudservice;



    @Autowired
    private UserRepository userRespository;

    @GetMapping("/user/{id}")
    @ResponseStatus(HttpStatus.FOUND)
    public User getUserbyid(@PathVariable int id){
        return userRespository.findById(id).orElse(null);

    }

    //should be change to token later on
    @PostMapping("/adduser")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse signup(@RequestBody User user){
        LOG.info("Token requested for user: '{}",user.getUsername());
        return userCrudservice.signup(user);
    }




    //the user id  be  set in frontend


    @DeleteMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable int id){
        LOG.info("Deleted user id: '{}",id);
        return userCrudservice.Delete(id);
    }







}
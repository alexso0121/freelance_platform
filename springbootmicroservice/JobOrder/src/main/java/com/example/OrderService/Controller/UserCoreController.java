package com.example.OrderService.Controller;


import com.example.OrderService.Entity.User;
import com.example.OrderService.Service.JobService;
import com.example.OrderService.Service.UserCoreService;
import com.example.OrderService.dto.InfoResponse;
import com.example.OrderService.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/UserJob")
@Slf4j
public class UserCoreController {

    private final UserCoreService userCoreservice;



    private final JobService jobService;

    public UserCoreController(UserCoreService userCoreservice, JobService jobService) {
        this.userCoreservice = userCoreservice;


        this.jobService = jobService;
    }

    @GetMapping("/Checkuser/{id}")
    public Boolean VerifyCanOrder(@PathVariable int id){
        return userCoreservice.VerifyCanOrder(id);
    }

    @GetMapping("/getProfile/{id}")
    public InfoResponse getProfile(@PathVariable int id){
        return userCoreservice.getProfile(id);
    }


    @PutMapping("/updateuser")
    public String UpdateUser (@RequestBody User user){
        log.info("Updated user: '{}",user.getId());
        return userCoreservice.updateUser(user);
    }


    @GetMapping("/application/history/{id}")
    public List<Response> showApplicationshistory(@PathVariable int id){
        return jobService.showApplications(id);
    }

    @GetMapping("/get/Byusername/{username}")
    public User getUserByName(@PathVariable String username){
        return userCoreservice.findByUsername(username);
    }

    @PostMapping("/add/user")
    public User addUser(@RequestBody User user){
        return userCoreservice.saveAndReturn(user);
    }

    @DeleteMapping("/deleteuser/{id}")
    public String deleteUser(@PathVariable int id){
        log.info("Deleted user id: '{}",id);
        return userCoreservice.Delete(id);
    }



}

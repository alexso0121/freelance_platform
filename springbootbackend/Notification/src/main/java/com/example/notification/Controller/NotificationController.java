package com.example.notification.Controller;

import com.example.notification.Entity.Notification;
import com.example.notification.Respository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;

    @PostMapping("/addNotice")
    public String addnotice(@RequestBody Notification notification){
        System.out.println(notification);
        notificationRepository.save(notification);
        return "userid: "+notification.getUser_id()+ " have one new notice ";
    }

    @GetMapping("/getNotice/{user_id}")
    public List<Notification> getNoticeByUser(@PathVariable int user_id){
        return notificationRepository.FindNoticeByUser_id(user_id);
    }
}

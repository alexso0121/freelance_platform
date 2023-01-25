package com.example.notification.Controller;


import com.example.notification.Respository.NotificationRepository;
import com.example.notification.dto.NoticeRespond;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")
public class NotificationController {

    @Autowired
    private NotificationRepository notificationRepository;




    @GetMapping("/getNotice/{user_id}")
    public List<NoticeRespond> getNoticeByUser(@PathVariable int user_id){
        return notificationRepository.FindNoticeByUser_id(user_id);
    }
}

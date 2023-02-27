package com.example.notification;

import com.example.notification.Respository.NotificationRepository;
import com.example.notification.dto.NoticeRespond;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
//@EnableDiscoveryClient
@EnableMongoRepositories
@Slf4j
public class NotificationApplication {
    @Autowired
    private NotificationRepository notificationRepository;

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }


    @KafkaListener(topics = "notificationTopic")
    public String addnotice(NoticeRespond notification){
        System.out.println(notification);
        notificationRepository.save(notification);
        return "userid: "+notification.getUser_id()+ " have one new notice ";
    }

}

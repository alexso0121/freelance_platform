package com.example.notification.Respository;

import com.example.notification.Entity.Notification;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<Notification,Integer> {
   @Query("{ user_id : ?0 }")
    List<Notification> FindNoticeByUser_id(int user_id);


}

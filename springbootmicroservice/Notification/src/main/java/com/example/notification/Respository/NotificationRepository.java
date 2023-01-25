package com.example.notification.Respository;

import com.example.notification.dto.NoticeRespond;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends MongoRepository<NoticeRespond,Integer> {
   @Query("{ user_id : ?0 }")
    List<NoticeRespond> FindNoticeByUser_id(int user_id);


}

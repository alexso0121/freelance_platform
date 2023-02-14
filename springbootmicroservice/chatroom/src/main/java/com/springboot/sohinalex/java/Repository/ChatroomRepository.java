package com.springboot.sohinalex.java.Repository;

import com.springboot.sohinalex.java.Entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface ChatroomRepository extends ReactiveCrudRepository<ChatMessage,Integer> {


    @Query("{ order_id : ?0 }")
    Flux<ChatMessage> getChatsByOrderId(int roomId);
}

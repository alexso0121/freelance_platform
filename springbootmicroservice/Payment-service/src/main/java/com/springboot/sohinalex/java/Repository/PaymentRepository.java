package com.springboot.sohinalex.java.Repository;

import com.springboot.sohinalex.java.Entity.Payment;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface PaymentRepository extends ReactiveMongoRepository<Payment, String> {

    @Query("{ user_id : ?0 }")
    Flux<Payment> findByUser_id(UUID userId);
}

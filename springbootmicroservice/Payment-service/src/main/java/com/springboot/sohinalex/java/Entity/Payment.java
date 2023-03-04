package com.springboot.sohinalex.java.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
public class Payment {
    private String charge_id;
    private UUID user_id;
    private PaymentType paymentType;
    private Double amount;
    private Instant dateTime;

    public enum PaymentType{
        CHARGE,       //when the job poster paid to our platform
        TRANSFER,       //our platform paid to the freelancer
        REFUND      //refund the payment to the job poster
    }

}

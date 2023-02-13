package com.example.OrderService.Service;

import com.example.OrderService.dto.NoticeRespond;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class kafkaProducer {
    private final KafkaTemplate<String, NoticeRespond> kafkaTemplate;

    public kafkaProducer(KafkaTemplate<String, NoticeRespond> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;

    }

    public Boolean sendNotice(String notification,int apply_id,int order_id){
        try{
            LocalDateTime myDateObj = LocalDateTime.now();
            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            String formattedDate = myDateObj.format(myFormatObj);
            kafkaTemplate.send("notificationTopic",new NoticeRespond(
                    apply_id,formattedDate,notification
            ));
            log.info(apply_id+"has applied job with id: "+order_id);
            return true;
        }
        catch (Exception exception){
            log.error("cant found the consumer");
            return false;
        }
    }
}

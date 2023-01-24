package com.example.notification.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.Documented;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "Notification")
public class Notification {

    private int user_id;

    private String Notice;

}

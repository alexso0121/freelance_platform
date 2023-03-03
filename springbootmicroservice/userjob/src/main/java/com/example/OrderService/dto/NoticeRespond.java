package com.example.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

//class for sending notification
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRespond {
    private UUID user_id;
    private String datetime;
    private String notification;
}

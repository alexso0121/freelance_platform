package com.example.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//class for sending notification
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRespond {
    private int user_id;
    private String datetime;
    private String notification;
}

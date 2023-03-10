package com.example.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRespond {
    private int user_id;
    private String datetime;
    private String notification;
}

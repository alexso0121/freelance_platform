package com.springboot.sohinalex.java.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRespond {
    private UUID user_id;
    private String datetime;
    private String notification;
}

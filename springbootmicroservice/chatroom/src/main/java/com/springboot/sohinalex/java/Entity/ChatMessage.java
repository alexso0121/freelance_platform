package com.springboot.sohinalex.java.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @Id
    private String id;
    private int order_id;
    private MessageType type;
    private String content;
    private String sender;
    private Instant dateTime;

    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE,
        BUILD
    }}
package com.example.OrderService.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;

@Data
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
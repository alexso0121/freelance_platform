package com.springboot.sohinalex.java.Service;

import com.springboot.sohinalex.java.Entity.ChatMessage;
import com.springboot.sohinalex.java.Repository.ChatroomRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class ChatroomService {
    private final ChatroomRepository chatroomRepository;


    public ChatroomService(ChatroomRepository chatroomRepository) {
        this.chatroomRepository = chatroomRepository;

    }


    public Mono<ChatMessage> sendMessage(ChatMessage chatMessage) {
        return chatroomRepository.save(chatMessage)
                .switchIfEmpty(Mono.error(new Error("cannot save message"))
                        );
    }

   /* public ChatMessage addUser(String roomId, ChatMessage chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        String currentRoomId = (String) headerAccessor.getSessionAttributes().put("room_id", roomId);

        if (currentRoomId != null) {
            ChatMessage JoinMessage = new ChatMessage();
            chatMessage.setDateTime(Instant.now());
            chatMessage.setOrder_id(roomId);
            JoinMessage.setType(ChatMessage.MessageType.JOIN);
            JoinMessage.setSender(chatMessage.getSender());
            JoinMessage.setContent(chatMessage.getSender()+"has entered the chat!");
            messagingTemplate.convertAndSend(format("/channel/%s", currentRoomId), JoinMessage);
            chatroomRepository.save(chatMessage);
        }

        return null;
    }*/

    public Flux<ChatMessage> getChatRecords(int roomId) {

        return chatroomRepository.getChatsByOrderId(roomId)
                .switchIfEmpty(Mono.error(new Error("no order_id found")));
    }}

   /* public ChatMessage buildRoom(String roomId,ChatMessage chatMessage) {
        chatMessage.setDateTime(Instant.now());
        chatMessage.setOrder_id(roomId);
        chatMessage.setType(ChatMessage.MessageType.JOIN);
        chatMessage.setSender(chatMessage.getSender());
        chatMessage.setContent(chatMessage.getSender()+"has entered the chat!");
        messagingTemplate.convertAndSend(format("/channel/%s", roomId), chatMessage);
        return chatroomRepository.save(chatMessage);

    }
}*/

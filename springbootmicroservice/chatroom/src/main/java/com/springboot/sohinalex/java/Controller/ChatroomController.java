package com.springboot.sohinalex.java.Controller;

import com.springboot.sohinalex.java.Entity.ChatMessage;
import com.springboot.sohinalex.java.Service.ChatroomService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/Chat")
public class ChatroomController {
    private final ChatroomService chatroomService;


    public ChatroomController(ChatroomService chatroomService) {
        this.chatroomService = chatroomService;

    }

    //including Type:chat,join,leave,build,delete
    @PostMapping("/chat/post")
    public Mono<ChatMessage> sendMessage(@RequestBody ChatMessage message) {
        return chatroomService.sendMessage(message)
                .switchIfEmpty(Mono.error(new Error("error")))
                .doOnNext(System.out::println)
                .map(res->{
                    res.setDateTime(Instant.now());
                    return res;
                });
    }



    @GetMapping("/chat/show/{order_id}")
    public Flux<ChatMessage> getChatRecords(@PathVariable String order_id){
        return chatroomService.getChatRecords(Integer.parseInt(order_id));
    }
    @GetMapping("/chat/isempty/{order_id}")
    public Flux<ChatMessage> IsEmpty(@PathVariable int order_id){
        return chatroomService.getChatRecords(order_id)
                .take(1);

    }

   /* @MessageMapping("/chat/{roomId}/buildroom")*/
    /*public ChatMessage buildRoom(@DestinationVariable String roomId, @Payload ChatMessage chatMessage){
        return chatroomService.buildRoom(roomId,chatMessage);
    }*/


    //build a chatroom

  /*  @PostMapping("/post")
    public Mono<Chat> PostChat(Chat chat){
        return chatroomService.PostChat(chat);
    }

    //get chat from sender
    @GetMapping
    public Flux<Chat> getChatroomSend(@RequestParam int senderId,int receiverId){
        return chatroomService.getChatroomSend(senderId,receiverId);
    }*/
}

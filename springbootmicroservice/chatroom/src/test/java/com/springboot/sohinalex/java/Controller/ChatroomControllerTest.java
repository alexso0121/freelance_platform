package com.springboot.sohinalex.java.Controller;

import com.springboot.sohinalex.java.Entity.ChatMessage;
import com.springboot.sohinalex.java.Repository.ChatroomRepository;
import com.springboot.sohinalex.java.Service.ChatroomService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@WebFluxTest(ChatroomController.class)
class ChatroomControllerTest {

    @MockBean
    private ChatroomService chatroomService;

    @Autowired
    private WebTestClient webClient;


    private ChatMessage MockMessage(String username){
        return ChatMessage.builder()
                .id("id").order_id(1).type(ChatMessage.MessageType.BUILD).content("test").sender(username)
                .dateTime(Instant.now()).build();
    }
    @Test
    void sendMessage() {
        ChatMessage message=MockMessage("admin");

        when(chatroomService.sendMessage(message)).thenReturn(Mono.just(message));

        webClient
                .post().uri("/Chat/chat/post")
                .bodyValue(message)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ChatMessage.class);


    }

    //@Test
    void getChatRecords() {
        ChatMessage message1=MockMessage("admin");
        ChatMessage message2=MockMessage("alex");
        when(chatroomService.getChatRecords(1)).thenReturn(Flux.just(message1,message2));

        webClient
                .get().uri("/Chat/chat/show/1")

               .exchange()
                .expectStatus()
                .isOk()
                .expectBody(ChatMessage.class);
    }

    @Test
    void isEmpty() {
    }
}
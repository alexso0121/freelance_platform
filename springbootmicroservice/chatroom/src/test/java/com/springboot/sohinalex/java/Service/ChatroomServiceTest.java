package com.springboot.sohinalex.java.Service;

import com.springboot.sohinalex.java.Entity.ChatMessage;
import com.springboot.sohinalex.java.Repository.ChatroomRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Instant;
import static org.mockito.Mockito.when;




//@ExtendWith(SpringExtension.class)
@ExtendWith(SpringExtension.class)
class ChatroomServiceTest {

    @Mock
    private ChatroomRepository chatroomRepository;

    @InjectMocks
    private ChatroomService chatroomService;


    private ChatMessage MockMessage(String username){
        return ChatMessage.builder()
                .id("id").order_id(1).type(ChatMessage.MessageType.BUILD).content("test").sender(username)
                .dateTime(Instant.now()).build();
    }

    @Test
    void sendMessage() {
        ChatMessage chatmessage=MockMessage("admin");
        when(chatroomRepository.save(chatmessage)).thenReturn(Mono.just(chatmessage));
        Mono<ChatMessage> result=chatroomService.sendMessage(chatmessage);

        StepVerifier
                .create(result)
                .consumeNextWith(newUser->{
                    Assertions.assertEquals(newUser.getSender(),"admin");
                })
                .verifyComplete();
    }

    @Test
    void getChatRecords() {
        ChatMessage chatMessage1=MockMessage("admin");
        ChatMessage chatMessage2=MockMessage("alex");

        when(chatroomRepository.getChatsByOrderId(1)).thenReturn(Flux.just(chatMessage1,chatMessage2));

        Flux<ChatMessage> chats=chatroomService.getChatRecords(1);

        StepVerifier
                .create(chats)
                .consumeNextWith(newUser->{
                    Assertions.assertEquals(newUser.getSender(),"admin");
                })
                .consumeNextWith(user2->{
                    Assertions.assertEquals(user2.getSender(),"alex");
                })
                .verifyComplete();

    }
}
package com.springboot.sohinalex.java.config;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/*@Configuration
@EnableWebSocketMessageBroker*/
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override       //register the endpoint
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
        registry.addEndpoint("/chat");

        //sockJs is for setting the STOMP =>send  message to who(subscribe)
    }

    @Override   //control  with "/app"  can access
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        // '/topic' is access the broker
        registry.enableSimpleBroker("/topic");
    }
}

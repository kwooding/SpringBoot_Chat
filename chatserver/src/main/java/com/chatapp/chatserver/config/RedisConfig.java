package com.chatapp.chatserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import com.chatapp.chatserver.service.RedisSubscriber;

@Configuration
public class RedisConfig{

    public static final String CHANNEL = "chat-messages";
    /*private final RedisSubscriber subscriber;

    public RedisConfig(RedisSubscriber subscriber){
        this.subscriber = subscriber;
    }*/

    // Redis channel where the publisher writes to and the Subscriber listens for updates
    @Bean
    public ChannelTopic chatTopic() {
        return new ChannelTopic(CHANNEL);
    }

    // Adapter used to trigger listner event for OnMessage
    @Bean
    public MessageListenerAdapter messageListenerAdapter(RedisSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }

    // Container setup for redis making the connection and sending to adapter for on listener
    @Bean
    public RedisMessageListenerContainer redisContainer(
            RedisConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter,
            ChannelTopic topic) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, topic);
        return container;
    }
}
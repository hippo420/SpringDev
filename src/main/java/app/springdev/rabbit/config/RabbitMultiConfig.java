package app.springdev.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMultiConfig {
    public static final String EXCHANGE_NAME = "multi_exchange";
    public static final String QUEUE_NOTI = "queue.noti";
    public static final String NOTI_KEY = "routing.noti";
    public static final String QUEUE_CHAT = "queue.chat";
    public static final String CHAT_KEY = "routing.chat";

    // 공통 Exchange
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // Queue 1
    @Bean
    public Queue queueNoti() {
        return new Queue(QUEUE_NOTI, true);
    }

    @Bean
    public Binding bindingQueueOne(Queue queueNoti, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueNoti).to(topicExchange).with(NOTI_KEY);
    }

    // Queue 2
    @Bean
    public Queue queueChat() {
        return new Queue(QUEUE_CHAT, true);
    }

    @Bean
    public Binding bindingQueueTwo(Queue queueChat, TopicExchange topicExchange) {
        return BindingBuilder.bind(queueChat).to(topicExchange).with(CHAT_KEY);
    }
}

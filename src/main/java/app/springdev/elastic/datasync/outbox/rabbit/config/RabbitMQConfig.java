package app.springdev.elastic.datasync.outbox.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    public static final String EXCHANGE_NAME = "noti.exchange";
    public static final String QUEUE_NAME = "noti.queue";
    public static final String ROUTING_KEY = "noti.created";

    @Bean
    public DirectExchange notiExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue notiQueue() {
        return new Queue(QUEUE_NAME, true); // durable = true
    }

    @Bean
    public Binding bindingNoti(Queue notiQueue, DirectExchange notiExchange) {
        return BindingBuilder.bind(notiQueue).to(notiExchange).with(ROUTING_KEY);
    }

    //실패 처리 (DLQ, Retry)
//    @Bean
//    public Queue notiDLQ() {
//        return QueueBuilder.durable("noti.dlq").build();
//    }
//
//    @Bean
//    public Binding dlqBinding() {
//        return BindingBuilder.bind(notiDLQ())
//                .to(new DirectExchange("noti.dlq.exchange"))
//                .with("noti.dlq");
//    }
}

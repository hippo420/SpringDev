package app.springdev.rabbit.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String EXCHANGE_NAME = "my_exchange";
    public static final String QUORUM_QUEUE_NAME = "trade.quorum.queue";
    public static final String ROUTING_KEY = "trade.key";



    // 1. Exchange 생성 (일반적인 Direct Exchange 사용)
    @Bean
    public DirectExchange  directExchange() {
        return new DirectExchange (EXCHANGE_NAME);
    }

    // 2. 🌟 Quorum Queue 생성 (실무 권장)
    @Bean
    public Queue quorumQueue() {
        return QueueBuilder.durable(QUORUM_QUEUE_NAME)
                // 큐 타입 설정: Classic 대신 Quorum 사용
                .withArgument("x-queue-type", "quorum")
                // TTL(Time-To-Live) 설정 등 필요한 인자 추가 가능
                .build();
    }

    // 3. Exchange와 Queue를 라우팅 키로 바인딩
    @Bean
    public Binding binding(DirectExchange directExchange, Queue quorumQueue) {
        return BindingBuilder.bind(quorumQueue).to(directExchange).with(ROUTING_KEY);
    }

    // 4. 메시지 변환기 (MessageConverter) 설정: JSON 직렬화/역직렬화
    // 실무에서는 문자열 대신 DTO 객체를 주고받기 위해 필수입니다.
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

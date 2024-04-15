package com.nrl.factory.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {
    private static final String DEAD_LETTER_QUEUE = "dead.letter.queue";
    private static final String DEAD_LETTER_EXCHANGE = "dead.letter.exchange";
    private static final String DEAD_LETTER_ROUTING_KEY = "dead.letter";

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("direct.exchange");
    }

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanout.exchange");
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topic.exchange");
    }

    @Bean
    public Queue topicQeueu(){
        return QueueBuilder.nonDurable("topic.queue").autoDelete().build();
    }

    @Bean
    public Queue directQueue() {
        return new Queue("direct.Queue");
    }

    /**@apiNote
     * exchange uzerinde kalan mesajlari 30 sn sonra dead queuye aktarir.
     * @return
     */
    @Bean
    public Queue fanoutQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", DEAD_LETTER_EXCHANGE);
        args.put("x-dead-letter-routing-key", DEAD_LETTER_ROUTING_KEY);
        args.put("x-message-ttl", 30000);
        return new Queue("fanout.queue", true, false, false, args);
    }
    @Bean
    public Queue deadLetterQueue() {
        return new Queue(DEAD_LETTER_QUEUE);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCHANGE);
    }

    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue()).to(deadLetterExchange()).with(DEAD_LETTER_ROUTING_KEY);
    }
    @Bean
    public Queue fanoutQueueSecond() {
        return new Queue("fanout.queue2");
    }


    @Bean
    public Binding bindinDirect(DirectExchange directExchange, Queue directQueue) {
        return BindingBuilder.bind(directQueue).to(directExchange).with("direct.routing.key");
    }

    @Bean
    public Binding bindingFanout(FanoutExchange fanoutExchange, Queue fanoutQueue) {
        return BindingBuilder.bind(fanoutQueue).to(fanoutExchange);
    }
    @Bean
    public Binding bindingFanoutSecond(FanoutExchange fanoutExchange, Queue fanoutQueueSecond) {
        return BindingBuilder.bind(fanoutQueueSecond).to(fanoutExchange);
    }
    @Bean
    public Binding bindingTopic(TopicExchange topicExchange, Queue topicQeueu){
        return BindingBuilder.bind(topicQeueu).to(topicExchange).with("topic.routing.key");
    }
}

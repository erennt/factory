package com.nrl.factory.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Service;

@Service
public class AlertService {

    private final Logger logger = LoggerFactory.getLogger(AlertService.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public ResponseEntity<String> sendAlarm(String message){
        try {
            run();
        }catch (Exception exception){
            logger.error("alarm send fail:",exception);
            return new ResponseEntity<>(
                    exception.getMessage(),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
    public void run()  {
        sendMessageToDirectExchange("This is a direct message", "direct.routing.key");
        sendMessageToFanoutExchange("This is a fanout message");
        sendMessageToTopicExchange("This is a topic message", "topic.routing.key");
    }

    private void sendMessageToDirectExchange(String message, String routingKey) {
        rabbitTemplate.convertAndSend("direct.exchange", routingKey, message);
        System.out.println("Sent direct message: " + message);
    }

    private void sendMessageToFanoutExchange(String message) {
        rabbitTemplate.convertAndSend("fanout.exchange", "", message);
        System.out.println("Sent fanout message: " + message);
    }

    private void sendMessageToTopicExchange(String message, String routingKey) {
        rabbitTemplate.convertAndSend("topic.exchange", routingKey, message);
        System.out.println("Sent topic message: " + message);
    }
    @Scheduled(fixedRate = 3000)
    public void scheduled(){
        run();
    }
}

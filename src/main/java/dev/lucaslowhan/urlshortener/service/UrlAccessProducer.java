package dev.lucaslowhan.urlshortener.service;

import dev.lucaslowhan.urlshortener.domain.UrlAccessEvent;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UrlAccessProducer {
    private final AmqpTemplate amqpTemplate;

    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    public UrlAccessProducer(AmqpTemplate amqpTemplate) {
        this.amqpTemplate = amqpTemplate;
    }

    public void publish(UrlAccessEvent event){
        amqpTemplate.convertAndSend(exchange,routingKey,event);
    }
}

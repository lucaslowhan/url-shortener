package dev.lucaslowhan.urlshortener.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value("${rabbitmq.queue.url-access}")
    private String queue;
    @Value("${rabbitmq.exchange}")
    private String exchange;
    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    @Bean
    public ApplicationRunner rabbitInitializer(ConnectionFactory connectionFactory) {
        return args -> {
            connectionFactory.createConnection().close();
            System.out.println(">>> RabbitMQ conectado com sucesso");
        };
    }

    @Bean
    public Queue urlAcessQueue(){
        System.out.println(">>> Criando fila: " + queue);
        return new Queue(queue, true);
    }

    @Bean
    public DirectExchange urlExchange(){
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding binding(Queue urlAcessQueue, DirectExchange urlExchange){
        return BindingBuilder
                .bind(urlAcessQueue)
                .to(urlExchange)
                .with(routingKey);
    }

    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter(new ObjectMapper());
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}

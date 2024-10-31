package com.ureca.config.rabbitMQ.event;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirstEventMQ extends EventRabbitMQConfig {

  private static final int QUEUE_NUMBER = 1;
  private static final String QUEUE_NAME = "eventQueue" + QUEUE_NUMBER;
  private static final String ROUTING_KEY = "eventRoutingKey" + QUEUE_NUMBER;
  private static final String DEAD_LETTER_EXCHANGE = "deadLetterExchange";

  @Bean
  public Queue FirstEventQueue() {
    return this.createQueue(QUEUE_NAME, DEAD_LETTER_EXCHANGE);
  }

  @Bean
  public Binding FirstEventBinding(Queue FirstEventQueue, DirectExchange eventExchange) {
    return BindingBuilder.bind(FirstEventQueue).to(eventExchange).with(ROUTING_KEY);
  }

  @Bean
  public CommandLineRunner initializeEventQueue(
      @Qualifier("eventRabbitAdmin") RabbitAdmin rabbitAdmin) {
    return args -> {
      rabbitAdmin.declareQueue(FirstEventQueue());
      rabbitAdmin.declareExchange(this.eventExchange());
      rabbitAdmin.declareBinding(FirstEventBinding(FirstEventQueue(), this.eventExchange()));
    };
  }
}

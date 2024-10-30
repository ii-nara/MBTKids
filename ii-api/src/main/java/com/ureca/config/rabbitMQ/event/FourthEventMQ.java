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
public class FourthEventMQ extends EventRabbitMQConfig {

  private static final int QUEUE_NUMBER = 4;
  private static final String QUEUE_NAME = "eventQueue" + QUEUE_NUMBER;
  private static final String ROUTING_KEY = "eventRoutingKey" + QUEUE_NUMBER;
  private static final String DEAD_LETTER_EXCHANGE = "deadLetterExchange";

  @Bean
  public Queue FourthEventQueue() {
    return this.createQueue(QUEUE_NAME, DEAD_LETTER_EXCHANGE);
  }

  @Bean
  public Binding FourthEventBinding(Queue FourthEventQueue, DirectExchange eventExchange) {
    return BindingBuilder.bind(FourthEventQueue).to(eventExchange).with(ROUTING_KEY);
  }

  @Bean
  public CommandLineRunner initializeEventQueue(
      @Qualifier("eventRabbitAdmin") RabbitAdmin rabbitAdmin) {
    return args -> {
      rabbitAdmin.declareQueue(FourthEventQueue());
      rabbitAdmin.declareExchange(this.eventExchange());
      rabbitAdmin.declareBinding(FourthEventBinding(FourthEventQueue(), this.eventExchange()));
    };
  }
}

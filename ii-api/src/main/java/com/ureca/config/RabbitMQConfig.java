package com.ureca.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  private static final String EXCHANGE_NAME = "feedbackExchange";
  private static final String QUEUE_NAME = "feedbackQueue";
  private static final String ROUTING_KEY = "feedbackRoutingKey";

  @Value("${spring.rabbitmq.host}")
  private String host;

  @Value("${spring.rabbitmq.port}")
  private int port;

  @Value("${spring.rabbitmq.username}")
  private String username;

  @Value("${spring.rabbitmq.password}")
  private String password;

  @Bean
  public Queue feedbackQueue() {
    return QueueBuilder.durable(QUEUE_NAME)
        .withArgument("x-dead-letter-exchange", "deadLetterExchange")
        .withArgument("x-max-priority", 10)
        .build();
  }

  @Bean
  public DirectExchange feedbackExchange() {
    return new DirectExchange(EXCHANGE_NAME);
  }

  @Bean
  public Binding feedbackBinding(Queue feedbackQueue, DirectExchange feedbackExchange) {
    return BindingBuilder.bind(feedbackQueue).to(feedbackExchange).with(ROUTING_KEY);
  }

  @Bean
  public Jackson2JsonMessageConverter messageConverter() {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    return new Jackson2JsonMessageConverter(objectMapper);
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(messageConverter());
    return template;
  }

  @Bean
  public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      ConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setErrorHandler(new ConditionalRejectingErrorHandler());
    factory.setMessageConverter(messageConverter());
    factory.setPrefetchCount(200);
    return factory;
  }

  @Bean
  public CommandLineRunner initializeQueue(RabbitAdmin rabbitAdmin) {
    return args -> {
      rabbitAdmin.declareQueue(feedbackQueue());
      rabbitAdmin.declareExchange(feedbackExchange());
      rabbitAdmin.declareBinding(feedbackBinding(feedbackQueue(), feedbackExchange()));
    };
  }

  @Bean
  public ConnectionFactory connectionFactory() {
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
    connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
    connectionFactory.setPort(port);
    connectionFactory.setUsername(username);
    connectionFactory.setPassword(password);
    connectionFactory.setChannelCacheSize(50);
    return connectionFactory;
  }

  @Bean
  public Queue deadLetterQueue() {
    return new Queue("deadLetterQueue", true);
  }

  @Bean
  public DirectExchange deadLetterExchange() {
    return new DirectExchange("deadLetterExchange");
  }

  @Bean
  public Binding deadLetterBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
    return BindingBuilder.bind(deadLetterQueue).to(deadLetterExchange).with("deadLetterRoutingKey");
  }
}

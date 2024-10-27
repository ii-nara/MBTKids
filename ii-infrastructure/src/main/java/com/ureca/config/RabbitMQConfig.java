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
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  private static final String EXCHANGE_NAME = "feedbackExchange";
  private static final String QUEUE_NAME = "feedbackQueue";
  private static final String ROUTING_KEY = "feedbackRoutingKey";

  @Bean
  public Queue feedbackQueue() {
    return new Queue(QUEUE_NAME, true);
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
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL); // Null 값 제외
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
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setErrorHandler(new ConditionalRejectingErrorHandler());
    factory.setMessageConverter(messageConverter());
    return factory;
  }

  @Bean
  public CommandLineRunner initializeQueue(RabbitAdmin rabbitAdmin) {
    return args -> {
      // feedbackQueue 삭제 후 재생성 및 다시 바인딩
      rabbitAdmin.deleteQueue("feedbackQueue");
      rabbitAdmin.declareQueue(feedbackQueue());
      rabbitAdmin.declareExchange(feedbackExchange());
      rabbitAdmin.declareBinding(feedbackBinding(feedbackQueue(), feedbackExchange()));
    };
  }

  @Bean
  public ConnectionFactory connectionFactory() {
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
    connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
    connectionFactory.setChannelCacheSize(50000);  // 적절한 캐시 크기 설정
    return connectionFactory;
  }
}

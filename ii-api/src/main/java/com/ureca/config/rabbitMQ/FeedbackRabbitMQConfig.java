package com.ureca.config.rabbitMQ;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeedbackRabbitMQConfig extends BaseRabbitMQConfig {

  private static final String EXCHANGE_NAME = "feedbackExchange";
  private static final String QUEUE_NAME = "feedbackQueue";
  private static final String ROUTING_KEY = "feedbackRoutingKey";
  private static final String DEAD_LETTER_EXCHANGE = "deadLetterExchange";
  private static final int PREFETCH_COUNT = 200; // 메시지를 동시에 처리할 수 있도록 적절히 조정
  private static final int CHANNEL_CACHE_SIZE = 50; // 채널 캐시 크기 조정 (1채널당 1,000~5,000 메시지)

  @Value("${spring.feedback.rabbitmq.host}")
  private String host;

  @Value("${spring.feedback.rabbitmq.port}")
  private int port;

  @Value("${spring.feedback.rabbitmq.username}")
  private String username;

  @Value("${spring.feedback.rabbitmq.password}")
  private String password;

  @Bean
  public Queue feedbackQueue() {
    return this.createQueue(QUEUE_NAME, DEAD_LETTER_EXCHANGE);
  }

  @Bean
  public DirectExchange feedbackExchange() {
    return new DirectExchange(EXCHANGE_NAME);
  }

  @Bean
  public Binding feedbackBinding(Queue feedbackQueue, DirectExchange feedbackExchange) {
    return BindingBuilder.bind(feedbackQueue).to(feedbackExchange).with(ROUTING_KEY);
  }

  @Bean(name = "feedbackRabbitTemplate")
  public RabbitTemplate rabbitTemplate(
      @Qualifier("feedbackConnectionFactory") ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(messageConverter());
    return template;
  }

  @Bean(name = "feedbackRabbitAdmin")
  public RabbitAdmin rabbitAdmin(
      @Qualifier("feedbackConnectionFactory") ConnectionFactory connectionFactory) {
    return new RabbitAdmin(connectionFactory);
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      @Qualifier("feedbackConnectionFactory") ConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setErrorHandler(new ConditionalRejectingErrorHandler());
    factory.setMessageConverter(messageConverter());
    factory.setPrefetchCount(PREFETCH_COUNT);
    return factory;
  }

  @Bean
  public CommandLineRunner initializeQueue(
      @Qualifier("feedbackRabbitAdmin") RabbitAdmin rabbitAdmin) {
    return args -> {
      rabbitAdmin.declareQueue(feedbackQueue());
      rabbitAdmin.declareExchange(feedbackExchange());
      rabbitAdmin.declareBinding(feedbackBinding(feedbackQueue(), feedbackExchange()));
    };
  }

  @Bean(name = "feedbackConnectionFactory")
  public ConnectionFactory feedbackConnectionFactory() {
    CachingConnectionFactory connectionFactory = new CachingConnectionFactory(host);
    connectionFactory.setCacheMode(CachingConnectionFactory.CacheMode.CHANNEL);
    connectionFactory.setPort(port);
    connectionFactory.setUsername(username);
    connectionFactory.setPassword(password);
    connectionFactory.setChannelCacheSize(CHANNEL_CACHE_SIZE);
    return connectionFactory;
  }
}

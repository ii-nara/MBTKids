package com.ureca.service;

import com.ureca.dto.RequestFeedbackDto;
import com.ureca.dto.ResponseFeedbackDto;
import com.ureca.entity.BookEntity;
import com.ureca.entity.ChildEntity;
import jakarta.annotation.Resource;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackComponentService {

  private final FeedbackManagementService feedbackManagementService;

  private final MbtiManagementService mbtiManagementService;

  private final ChildService childService;

  @Resource(name = "feedbackRabbitTemplate")
  private final RabbitTemplate rabbitTemplate;

  @Transactional
  @RabbitListener(queues = "feedbackQueue")
  public void addFeedbackMQ(@Payload RequestFeedbackDto requestFeedbackDto) {
    feedbackUpdate(requestFeedbackDto);
  }

  public void addFeedback(RequestFeedbackDto requestFeedbackDto) {
    rabbitTemplate.convertAndSend("feedbackExchange", "feedbackRoutingKey", requestFeedbackDto);
  }

  public void feedbackUpdate(RequestFeedbackDto requestFeedbackDto) {
    BookEntity book = feedbackManagementService.getBookById(requestFeedbackDto.getBookId());
    ChildEntity child = childService.findChildById(requestFeedbackDto.getChildId());
    ResponseFeedbackDto responseFeedbackDto =
        feedbackManagementService.addFeedbackStatus(
            book, child, requestFeedbackDto.getLikeStatusValue());
    mbtiManagementService.updateMbtiStatus(child, responseFeedbackDto);
  }
}

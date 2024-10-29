package com.ureca.service;

import com.ureca.dto.RequestFeedbackDto;
import com.ureca.dto.ResponseFeedbackDto;
import com.ureca.entity.BookEntity;
import com.ureca.entity.ChildEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackComponentService {

  private final FeedbackManagementService feedbackManagementService;

  private final MbtiManagementService mbtiManagementService;

  private final ChildService childService;

  public void addFeedback(RequestFeedbackDto requestFeedbackDto) {
    BookEntity book = feedbackManagementService.getBookById(requestFeedbackDto.getBookId());
    ChildEntity child = childService.findChildById(requestFeedbackDto.getChildId());
    ResponseFeedbackDto responseFeedbackDto =
        feedbackManagementService.addFeedbackStatus(
            book, child, requestFeedbackDto.getLikeStatusValue());
    mbtiManagementService.updateMbtiStatus(child, responseFeedbackDto);
  }

  @Transactional
  @RabbitListener(queues = "feedbackQueue")
  public void handleFeedback(@Payload RequestFeedbackDto requestFeedbackDto) {
    addFeedback(requestFeedbackDto);
  }
}

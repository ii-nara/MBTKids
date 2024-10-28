package com.ureca.service;

import com.ureca.dto.RequestFeedbackDto;
import com.ureca.dto.ResponseFeedbackDto;
import com.ureca.entity.ChildEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackComponentService {

  private final FeedbackManagementService feedbackManagementService;

  private final MbtiManagementService mbtiManagementService;

  public void addFeedback(RequestFeedbackDto requestFeedbackDto) {
    ChildEntity child = feedbackManagementService.getChildById(requestFeedbackDto.getChildId());
    ResponseFeedbackDto responseFeedbackDto = feedbackManagementService.addFeedbackStatus(requestFeedbackDto);
    mbtiManagementService.updateMbtiStatus(child, responseFeedbackDto);
  }

  @Transactional
  @RabbitListener(queues = "feedbackQueue")
  @Retryable(maxAttempts = 1, backoff = @Backoff(delay = 1000))
  public void handleFeedback(@Payload RequestFeedbackDto requestFeedbackDto) {
    addFeedback(requestFeedbackDto);
  }

  public String findFeedbackStatus(Long bookId, Long childId) {
    return feedbackManagementService.findFeedbackStatus(bookId, childId);
  }

}

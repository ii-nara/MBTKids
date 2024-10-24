package com.ureca.service;

import com.ureca.dto.RequestFeedbackDto;
import com.ureca.dto.ResponseFeedbackDto;
import com.ureca.entity.ChildEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class FeedbackComponentService {

  private final FeedbackManagementService feedbackManagementService;

  private final MbtiManagementService mbtiManagementService;

  @Transactional
  public void addFeedback(@RequestBody RequestFeedbackDto requestFeedbackDto) {
    ChildEntity child = feedbackManagementService.getChildById(requestFeedbackDto.getChildId());
    ResponseFeedbackDto responseFeedbackDto = feedbackManagementService.addFeedbackStatus(requestFeedbackDto);
    mbtiManagementService.updateMbtiStatus(child, responseFeedbackDto);
  }

}

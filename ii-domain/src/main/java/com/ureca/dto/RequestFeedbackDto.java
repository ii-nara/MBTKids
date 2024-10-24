package com.ureca.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestFeedbackDto {

  private Long childId;

  private Long bookId;

  private Integer LikeStatusValue;

  public static RequestFeedbackDto of(Long childId, Long bookId, Integer LikeStatusValue) {
    return RequestFeedbackDto.builder()
        .bookId(bookId)
        .childId(childId)
        .LikeStatusValue(LikeStatusValue)
        .build();
  }

}
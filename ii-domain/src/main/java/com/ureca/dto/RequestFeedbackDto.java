package com.ureca.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RequestFeedbackDto {

  @JsonProperty("childId")
  private Long childId;

  @JsonProperty("bookId")
  private Long bookId;

  @JsonProperty("likeStatusValue")
  private Integer LikeStatusValue;

  public static RequestFeedbackDto of(Long childId, Long bookId, Integer LikeStatusValue) {
    return RequestFeedbackDto.builder()
        .bookId(bookId)
        .childId(childId)
        .LikeStatusValue(LikeStatusValue)
        .build();
  }

  public void updateChildId(Long childId) {
    this.childId = childId;
  }

  @JsonCreator
  public RequestFeedbackDto(@JsonProperty("childId") Long childId,
      @JsonProperty("bookId") Long bookId,
      @JsonProperty("LikeStatusValue") Integer likeStatusValue) {
    this.childId = childId;
    this.bookId = bookId;
    this.LikeStatusValue = likeStatusValue;
  }
}
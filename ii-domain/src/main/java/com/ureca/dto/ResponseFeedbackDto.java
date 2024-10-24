package com.ureca.dto;

import com.ureca.entity.Enum.LikeStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ResponseFeedbackDto {

  private Long bookId;

  private Integer feedbackValue;

  private Integer bookIE;

  private Integer bookSN;

  private Integer bookTF;

  private Integer bookPJ;

  private LikeStatus likeStatus;

  public static ResponseFeedbackDto of(Long bookId, Integer feedbackValue, Integer bookIE, Integer bookSN, Integer bookTF, Integer bookPJ, LikeStatus likeStatus) {
    return ResponseFeedbackDto.builder()
        .bookId(bookId)
        .feedbackValue(feedbackValue)
        .bookIE(bookIE)
        .bookSN(bookSN)
        .bookTF(bookTF)
        .bookPJ(bookPJ)
        .likeStatus(likeStatus)
        .build();
  }


}

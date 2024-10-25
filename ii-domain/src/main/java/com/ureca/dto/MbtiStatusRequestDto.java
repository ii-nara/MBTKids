package com.ureca.dto;

import lombok.Builder;
import lombok.Getter;

/* MBTI 결과 응답 */
@Getter
@Builder
public class MbtiStatusRequestDto {
  // 자녀 아이디
  // private final Integer childId;
  // 성향
  private final String mbti;
  // 강도
  private final Integer scoreIE;
  private final Integer scoreSN;
  private final Integer scoreTF;
  private final Integer scorePJ;
}
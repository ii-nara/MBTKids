package com.ureca.model;

import lombok.Builder;
import lombok.Getter;

/* 개별 MBTI 질문 구조 */
@Getter
public class MbtiQuestion {

  // 순서
  private final int order;
  // 질문 내용
  private final String content;
  // 질문별 성향 (약한/강한)
  private final String type;

  @Builder
  public MbtiQuestion(int order, String content, String type) {
    this.order = order;
    this.content = content;
    this.type = type;
  }
}

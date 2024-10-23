package com.ureca.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

/* MBTI 결과 응답 */
@Getter
@Builder
public class MbtiResponseDTO {

  // 성향
  private final String MBTI;
  // 강도 [I/E][S/N][T/F][P/J]
  private final List<Integer> score;
}

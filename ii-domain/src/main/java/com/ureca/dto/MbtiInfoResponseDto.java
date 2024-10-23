package com.ureca.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MbtiInfoResponseDto {

  private final String mbtiNm;
  private final String emoji;
  private final String suffix;
  private final String content;
  private final String tag;
}

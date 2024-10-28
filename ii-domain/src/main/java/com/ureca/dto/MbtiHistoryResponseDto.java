package com.ureca.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MbtiHistoryResponseDto {

  int typeE;
  int typeI;
  int typeS;
  int typeN;
  int typeT;
  int typeF;
  int typeJ;
  int typeP;

  String MbtiType;
  LocalDateTime updateAt;
}

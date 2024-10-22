package com.ureca.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MbtiStatusResponseDto {

  int typeE;
  int typeI;
  int typeS;
  int typeN;
  int typeT;
  int typeF;
  int typeJ;
  int typeP;

  String MbtiType;
}

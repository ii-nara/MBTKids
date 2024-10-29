package com.ureca.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReqParentAddInfoDto {

  private final String userName;
  private final String phoneNumber;
  private final boolean infoAgreeYn;

}

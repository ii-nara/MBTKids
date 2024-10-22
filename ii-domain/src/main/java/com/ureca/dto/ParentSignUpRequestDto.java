package com.ureca.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ParentSignUpRequestDto {

  private final String email;
  private final String parentLoginId;
  private final String password;
  private final String userName;
  private final String phoneNumber;
  private final String provider;
  private final boolean infoAgreeYn;
  
}
package com.ureca.dto;

import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class ParentSignUpRequestDto {

  private final String email;
  private final String parentLoginId;
  private final String password;
  private final String userName;
  private final String phoneNumber;
  private final String provider;
  private final LocalDateTime createAt;
  private final boolean infoAgreeYn;

  @Builder
  public ParentSignUpRequestDto(String email, String parentLoginId, String password,
      String userName, String phoneNumber, String provider, LocalDateTime createAt,
      boolean infoAgreeYn) {
    this.email = email;
    this.parentLoginId = parentLoginId;
    this.password = password;
    this.userName = userName;
    this.phoneNumber = phoneNumber;
    this.provider = provider;
    this.createAt = createAt;
    this.infoAgreeYn = infoAgreeYn;

//    System.out.println("ParentSignUpRequestDto 생성 시 이메일 값: " + this.email);
  }

}
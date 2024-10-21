package com.ureca.domain;

import com.ureca.dto.ParentSignUpRequestDto;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
public class Parent {

  private final String email;
  private final String parentLoginId;
  private final String password;
  private final String userName;
  private final String phoneNumber;
  private final String provider;
  private final LocalDateTime createAt;
  private final boolean isActive;
  private final boolean infoAgreeYn;

  @Builder
  public Parent(String email, String parentLoginId, String password, String userName,
      String phoneNumber, String provider, LocalDateTime createAt, boolean isActive,
      boolean infoAgreeYn) {
    this.email = email;
    this.parentLoginId = parentLoginId;
    this.password = password;
    this.userName = userName;
    this.phoneNumber = phoneNumber;
    this.provider = provider;
    this.createAt = createAt;
    this.isActive = isActive;
    this.infoAgreeYn = infoAgreeYn;
  }

  public static Parent from(ParentSignUpRequestDto dto) {
    return Parent.builder()
        .email(dto.getEmail())
        .parentLoginId(dto.getParentLoginId())
        .password(dto.getPassword())
        .userName(dto.getUserName())
        .phoneNumber(dto.getPhoneNumber())
        .provider(dto.getProvider())
        .createAt(LocalDateTime.now())
        .isActive(true)
        .infoAgreeYn(dto.isInfoAgreeYn())
        .build();
  }

}

package com.ureca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parent")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ParentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "parentId")
  private Long parentId;

  @OneToMany
  @JoinColumn(name = "parentId")
  private List<ChildEntity> children = new ArrayList<>();

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "parentLoginId", nullable = false)
  private String parentLoginId;

  @Column(name = "password", nullable = false)
  private String password;

  @Column(name = "userName")
  private String userName;

  @Column(name = "phoneNumber")
  private String phoneNumber;

  @Column(name = "provider")
  private String provider;

  @Column(name = "createdAt", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "isActive")
  private boolean isActive;

  @Column(name = "infoAgreeYn")
  private boolean infoAgreeYn;


  public static ParentEntity createParent(String email, String parentLoginId, String password,
      String userName,
      String phoneNumber, String provider,
      LocalDateTime createdAt, boolean infoAgreeYn) {
    return ParentEntity.builder()
        .email(email)
        .parentLoginId(parentLoginId)
        .password(password)
        .userName(userName)
        .phoneNumber(phoneNumber)
        .provider(provider)
        .createdAt(createdAt)
        .isActive(true)
        .infoAgreeYn(infoAgreeYn)
        .build();

  }
}

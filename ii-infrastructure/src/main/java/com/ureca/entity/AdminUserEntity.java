package com.ureca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 관리자 정보
@Entity
@Table(name = "admin_user")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminUserEntity {

  // 관리자 아이디
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "adminId")
  private int adminId;

  // 관리자 로그인 아이디
  @Column(name = "adminLoginId", length = 20)
  private String adminLoginId;

  // 비밀번호
  @Column(name = "password", length = 200)
  private String password;

  // 가입일자
  @Column(name = "createdAt")
  @Temporal(TemporalType.DATE)
  private Date createdAt;

  @Builder
  public AdminUserEntity(
      int adminId,
      String adminLoginId,
      String password,
      Date createdAt) {
    this.adminId = adminId;
    this.adminLoginId = adminLoginId;
    this.password = password;
    this.createdAt = createdAt;
  }
}

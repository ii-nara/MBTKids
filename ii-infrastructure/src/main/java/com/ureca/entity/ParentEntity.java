package com.ureca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "parent")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ParentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "parentId")
  private Long parentId;

//  @OneToMany(mappedBy = "parent")
//  private List<ChildEntity> children = new ArrayList<>();

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "paretnLoginId", nullable = false)
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

  @Column(name = "isActive", nullable = false)
  private boolean isActive;

  @Column(name = "infoAgree")
  private String infoAgree;

  @Builder
  public ParentEntity(Long parentId, String email, String parentLoginId, String password,
      String userName, String phoneNumber, String provider, LocalDateTime createdAt,
      boolean isActive, String infoAgree) {
    this.parentId = parentId;
    this.email = email;
    this.parentLoginId = parentLoginId;
    this.password = password;
    this.userName = userName;
    this.phoneNumber = phoneNumber;
    this.provider = provider;
    this.createdAt = createdAt;
    this.isActive = isActive;
    this.infoAgree = infoAgree;
  }
}

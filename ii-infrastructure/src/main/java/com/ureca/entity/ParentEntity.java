package com.ureca.entity;

import com.ureca.domain.Parent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
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

  @OneToMany(mappedBy = "parent")
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

  public static ParentEntity from(Parent parent) {
    ParentEntity parentEntity = new ParentEntity();
    parentEntity.email = parent.getEmail();
    parentEntity.parentLoginId = parent.getParentLoginId();
    parentEntity.password = parent.getPassword();
    parentEntity.userName = parent.getUserName();
    parentEntity.phoneNumber = parent.getPhoneNumber();
    parentEntity.provider = parent.getProvider();
    parentEntity.createdAt = parent.getCreateAt();
    parentEntity.isActive = parent.isActive();
    parentEntity.infoAgreeYn = parent.isInfoAgreeYn();

    return parentEntity;
  }

  public Parent toModel() {
    return Parent.builder()
        .email(email)
        .parentLoginId(parentLoginId)
        .password(password)
        .userName(userName)
        .phoneNumber(phoneNumber)
        .provider(provider)
        .createAt(createdAt)
        .isActive(isActive)
        .infoAgreeYn(infoAgreeYn)
        .build();
  }
}

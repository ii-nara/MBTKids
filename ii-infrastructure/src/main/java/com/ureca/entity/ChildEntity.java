package com.ureca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "child_user")
public class ChildEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "childId", nullable = false)
  private Long childId;

  @Column(name = "parentId", nullable = false)
  private Long parentId;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "typeId")
  private MbtiStatusEntity mbtiStatusEntity;

  @Column(name = "childName", nullable = false)
  private String childName;

  @Column(name = "childAge", nullable = false)
  private int childAge;

  @UpdateTimestamp
  @Column(name = "createdAt", nullable = false)
  private LocalDateTime createdAt;

  @Builder
  public ChildEntity(Long parentId, MbtiStatusEntity mbtiStatusEntity, String childName,
      int childAge,
      LocalDateTime createdAt) {
    this.parentId = parentId;
    this.mbtiStatusEntity = mbtiStatusEntity;
    this.childName = childName;
    this.childAge = childAge;
    this.createdAt = createdAt;
  }

  public void setMbtiStatusEntity(MbtiStatusEntity mbtiStatusEntity) {
    this.mbtiStatusEntity = mbtiStatusEntity;
  }
}

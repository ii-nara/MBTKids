package com.ureca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "mbti_delete")
public class MbtiDeleteEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "deleteId", nullable = false)
  private Long deleteId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "childId")
  private ChildEntity child;

  @Column(name = "deleteDate", nullable = false)
  private LocalDateTime deleteDate;

  @Builder
  public MbtiDeleteEntity(ChildEntity child, LocalDateTime deleteDate) {
    this.child = child;
    this.deleteDate = deleteDate;
  }
}

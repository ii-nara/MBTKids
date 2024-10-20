package com.ureca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "mbti_history")
@Getter
@NoArgsConstructor
public class MbtiHistoryEntity {

  @Id
  @Column(name = "historyId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long historyId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "childId")
  private ChildEntity childEntity;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "logId")
  private FeedbackLogEntity feedbackLogEntity;

  @Column(name = "typeIE", nullable = false)
  @Min(1)
  @Max(10)
  private Integer typeIE;

  @Column(name = "typeSN", nullable = false)
  @Min(1)
  @Max(10)
  private Integer typeSN;

  @Column(name = "typeTF", nullable = false)
  @Min(1)
  @Max(10)
  private Integer typeTF;

  @Column(name = "typePJ", nullable = false)
  @Min(1)
  @Max(10)
  private Integer typePJ;

  @UpdateTimestamp
  @Column(name = "timeStamp", nullable = false)
  private LocalDateTime timeStamp;

  @Builder
  public MbtiHistoryEntity(ChildEntity childEntity, FeedbackLogEntity feedbackLogEntity
      , Integer typeIE, Integer typeSN, Integer typeTF, Integer typePJ) {
    this.childEntity = childEntity;
    this.feedbackLogEntity = feedbackLogEntity;
    this.typeIE = typeIE;
    this.typeSN = typeSN;
    this.typeTF = typeTF;
    this.typePJ = typePJ;
  }
}

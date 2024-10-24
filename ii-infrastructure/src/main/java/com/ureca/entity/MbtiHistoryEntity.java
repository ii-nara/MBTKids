package com.ureca.entity;

import com.ureca.entity.Enum.LikeStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

  @Column(name = "bookId", nullable = false)
  private Long bookId;

  @Column(name = "isLike", nullable = false)
  @Enumerated(EnumType.STRING)
  private LikeStatus isLike;

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
  @Column(name = "updateAt", nullable = false)
  private LocalDateTime timeStamp;

  @Builder
  public MbtiHistoryEntity(Long bookId, LikeStatus isLike, Integer typeIE, Integer typeSN, Integer typeTF, Integer typePJ) {
    this.bookId = bookId;
    this.isLike = isLike;
    this.typeIE = typeIE;
    this.typeSN = typeSN;
    this.typeTF = typeTF;
    this.typePJ = typePJ;
  }
}
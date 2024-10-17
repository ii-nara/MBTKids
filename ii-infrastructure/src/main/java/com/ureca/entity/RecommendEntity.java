package com.ureca.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "recommend")
public class RecommendEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long recommendId;
  private Long childId;
  private Long bookId;
  private LocalDateTime recommendAt;
  private String deleteYn;

  @Builder
  public RecommendEntity(Long childId, Long bookId, LocalDateTime recommendAt, String deleteYn) {
    this.childId = childId;
    this.bookId = bookId;
    this.recommendAt = recommendAt;
    this.deleteYn = deleteYn;
  }
}

package com.ureca.entity;

import com.ureca.entity.Enum.MbtiType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "mbti_history")
@Getter
@NoArgsConstructor
public class MbtiHistoryEntity {

  @Id
  @Column(name = "typeId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long historyId;

  @Column(name = "childId", nullable = false)
  private Long childId;

  //  @ManyToOne
  //  @JoinColumn(name = "childid", nullable = false)
  //  private Child child;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private MbtiType mbtiType;

  @CreatedDate
  @Column(name = "createAt")
  private LocalDateTime createAt;

  @Builder
  public MbtiHistoryEntity(Long childId, MbtiType mbtiType, LocalDateTime createAt) {
    this.childId = childId;
    this.mbtiType = mbtiType;
  }
}

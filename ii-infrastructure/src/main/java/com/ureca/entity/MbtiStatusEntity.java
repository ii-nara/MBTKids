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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "mbti_status")
@Getter
@NoArgsConstructor
public class MbtiStatusEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "typeId")
  private Long typeId;

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
  private LocalDateTime updateAt;

  @Builder
  public MbtiStatusEntity(Integer typeIE, Integer typeSN, Integer typeTF,
      Integer typePJ) {
    this.typeIE = typeIE;
    this.typeSN = typeSN;
    this.typeTF = typeTF;
    this.typePJ = typePJ;
  }

  public void updateMbtiType(Integer typeIE, Integer typeSN, Integer typeTF, Integer typePJ) {
    this.typeIE = typeIE;
    this.typeSN = typeSN;
    this.typeTF = typeTF;
    this.typePJ = typePJ;
  }

}
package com.ureca.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
  private Long mbtiStausId;

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

  @Column(name = "deleteAt")
  private LocalDateTime deleteAt;

  @OneToOne
  @JoinColumn(name="childId")
  private ChildEntity childEntity;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "mbti_statusId")
  private List<MbtiHistoryEntity> MbtiHistoryEntities = new ArrayList<>();

  @Builder
  public MbtiStatusEntity(Integer typeIE, Integer typeSN, Integer typeTF, Integer typePJ, ChildEntity childEntity) {
    this.typeIE = typeIE;
    this.typeSN = typeSN;
    this.typeTF = typeTF;
    this.typePJ = typePJ;
    this.updateAt = LocalDateTime.now();
    this.deleteAt = null;
    this.childEntity = childEntity;
  }

  public void updateMbtiType(Integer typeIE, Integer typeSN, Integer typeTF, Integer typePJ) {
    this.typeIE = typeIE;
    this.typeSN = typeSN;
    this.typeTF = typeTF;
    this.typePJ = typePJ;
    this.updateAt = LocalDateTime.now();
  }

  public void updateDeleteMbtiStauts() {
    this.updateAt = LocalDateTime.now();
  }

  public void addHistory(MbtiHistoryEntity mbtiHistoryEntity) {
    this.MbtiHistoryEntities.add(mbtiHistoryEntity);
  }

  public void setDeleteAt(LocalDateTime deleteAt) {
    this.deleteAt = deleteAt;
  }
}
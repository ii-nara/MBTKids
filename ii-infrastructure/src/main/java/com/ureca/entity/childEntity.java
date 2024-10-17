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
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "child_user")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class childEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "childId")
  private Long childId;


  //  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "parentId")
  @Column(name = "parentId", nullable = false)
  private Long parentId;

  @Column(name = "childName", nullable = false)
  private String childName;

  @Column(name = "childAge", nullable = false)
  private int childAge;

  @Column(name = "nickName")
  private String nickName;

  @Column(name = "type")
  private String type;

  @Column(name = "typeIE")
  private int typeIE;

  @Column(name = "typeSN")
  private int typeSN;

  @Column(name = "typeTF")
  private int typeTF;

  @Column(name = "typePJ")
  private int typePJ;

  @Column(name = "createdAt", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "isTypeDeleted", nullable = false)
  @ColumnDefault("false")
  private boolean isTypeDeleted;

  @Column(name = "deleteReqAt")
  private LocalDateTime deleteReqAt;

  @Builder
  public childEntity(Long parentId, String childName, int childAge, String nickName,
      String type, int typeIE, int typeSN, int typeTF, int typePJ, LocalDateTime createdAt,
      boolean isTypeDeleted, LocalDateTime deleteReqAt) {
    this.parentId = parentId;
    this.childName = childName;
    this.childAge = childAge;
    this.nickName = nickName;
    this.type = type;
    this.typeIE = typeIE;
    this.typeSN = typeSN;
    this.typeTF = typeTF;
    this.typePJ = typePJ;
    this.createdAt = createdAt;
    this.isTypeDeleted = isTypeDeleted;
    this.deleteReqAt = deleteReqAt;
  }

}

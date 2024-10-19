package com.ureca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "child_user")
public class ChildEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "childId", nullable = false)
  private Long childId;

  @ManyToOne
  @JoinColumn(name = "parentId")
  private ParentEntity parent;

  @Column(name = "childName", nullable = false)
  private String childName;

  @Column(name = "childAge", nullable = false)
  private int childAge;

  @Column(name = "createdAt", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "isTypeDeleted", nullable = false)
  @ColumnDefault("false")
  private boolean isTypeDeleted;
}

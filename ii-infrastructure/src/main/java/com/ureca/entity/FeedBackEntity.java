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
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

@Entity
@Table(name = "feedback")
@Getter
@NoArgsConstructor
public class FeedBackEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "feedbackId")
  private Long feedbackId;

  @Column(name = "childId", nullable = false)
  private Long childId;

  @Column(name = "bookId", nullable = false)
  private Long bookId;

//  @ManyToOne
//  @JoinColumn(name = "childid", nullable = false)
//  private Child child;

//  @ManyToOne
//  @JoinColumn(name = "bookId", nullable = false)
//  private Book bookId;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "islike", nullable = false)
  private LikeStatus isLike;

  @LastModifiedDate
  @Column(name = "updateat", nullable = false)
  private LocalDateTime updateAt;

  @Builder
  public FeedBackEntity(Long childId, Long bookId, LikeStatus isLike, LocalDateTime updateAt) {
    this.childId = childId;
    this.bookId = bookId;
    this.isLike = isLike;
  }

}

package com.ureca.entity;

import com.ureca.entity.Enum.LikeStatus;
import com.ureca.entity.Enum.LikeStatusConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "feedback_status")
@Getter
@NoArgsConstructor
public class FeedbackStatusEntity {

  @Id
  @Column(name = "feedbackId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long feedbackId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "childId")
  private ChildEntity childEntity;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "bookId")
  private BookEntity bookEntity;

  @Column(name = "isLike", nullable = false)
  @Convert(converter = LikeStatusConverter.class)
  private LikeStatus isLike;

  @UpdateTimestamp
  @Column(name = "updateAt", nullable = false)
  private LocalDateTime updateAt;

  @Builder
  public FeedbackStatusEntity(ChildEntity childEntity, BookEntity bookEntity, LikeStatus isLike) {
    this.childEntity = childEntity;
    this.bookEntity = bookEntity;
    this.isLike = isLike;
  }

}
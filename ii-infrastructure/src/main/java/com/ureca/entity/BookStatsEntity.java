package com.ureca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 도서 통계
@Entity
@Table(name = "book_stats")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookStatsEntity {

  // 통계 아이디
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "statsId", nullable = false)
  private Long statsId;

  // 도서 아이디
  @Column(name = "bookId", nullable = false)
  private Long bookId;

  // 도서명
  @Column(name = "bookName", length = 100)
  private String bookName;

  // 출판사
  @Column(name = "publisher", length = 50)
  private String publisher;

  // 좋아요 개수
  @Column(name = "likeCnt")
  private Integer likeCnt;

  // 싫어요 개수
  @Column(name = "disLikeCnt")
  private Integer disLikeCnt;

  // 평균 연령대
  @Column(name = "avgAge", length = 20)
  private String avgAge;

  // 평균 성향
  @Column(name = "avgMbti", length = 20)
  private String avgMbti;

  // 통계 출력 일자
  @Column(name = "statsAt")
  @Temporal(TemporalType.DATE)
  private LocalDate statsAt;

  @Builder
  public BookStatsEntity(
      Long bookId,
      String bookName,
      String publisher,
      Integer likeCnt,
      Integer disLikeCnt,
      String avgAge,
      String avgMbti,
      LocalDate statsAt) {
    this.bookId = bookId;
    this.bookName = bookName;
    this.publisher = publisher;
    this.likeCnt = likeCnt;
    this.disLikeCnt = disLikeCnt;
    this.avgAge = avgAge;
    this.avgMbti = avgMbti;
    this.statsAt = statsAt;
  }
}

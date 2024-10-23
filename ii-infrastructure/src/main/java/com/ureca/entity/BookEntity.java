package com.ureca.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// 도서 정보
@Entity
@Table(name = "book")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookEntity {

  // 도서 아이디
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "bookId", nullable = false)
  private Long bookId;

  // 도서명
  @Column(name = "bookName", length = 50)
  private String bookName;

  // 이미지 URL
  @Column(name = "bookImgUrl", length = 300)
  private String bookImgUrl;

  // 줄거리
  @Column(name = "plot", length = 300)
  private String plot;

  // 작가명
  @Column(name = "writer", length = 50)
  private String writer;

  // 작가 코드
  @Column(name = "writerCd", length = 20)
  private String writerCd;

  // 출판사
  @Column(name = "publisher", length = 50)
  private String publisher;

  // 출판사 코드
  @Column(name = "publisherCd", length = 20)
  private String publisherCd;

  // 권장연령
  @Column(name = "recommenedAge", length = 20)
  private String recommenedAge;

  // 앞 : -1, 뒤 : 1, 상태없음 : 0
  // I/E
  @Column(name = "typeIE")
  private int typeIE;

  // S/N
  @Column(name = "typeSN")
  private int typeSN;

  // T/F
  @Column(name = "typeTF")
  private int typeTF;

  // P/J
  @Column(name = "typePJ")
  private int typePJ;

  // 노출여부
  @Column(name = "displayYn", length = 4)
  private String displayYn;

  // 등록자
  @Column(name = "createId")
  private Long createId;

  // 등록일자
  @Column(name = "createdAt")
  @Temporal(TemporalType.DATE)
  private Date createdAt;

  // 수정자
  @Column(name = "updateId")
  private Long updateId;

  // 수정일자
  @Column(name = "updateAt")
  @Temporal(TemporalType.DATE)
  private Date updateAt;

  @Builder
  public BookEntity(
      Long bookId,
      String bookName,
      String bookImgUrl,
      String plot,
      String writer,
      String writerCd,
      String publisher,
      String publisherCd,
      String recommenedAge,
      int typeIE,
      int typeSN,
      int typeTF,
      int typePJ,
      Date createdAt,
      Date updateAt,
      Long updateId,
      Long createId,
      String displayYn) {
    this.bookId = bookId;
    this.bookName = bookName;
    this.bookImgUrl = bookImgUrl;
    this.plot = plot;
    this.writer = writer;
    this.writerCd = writerCd;
    this.publisher = publisher;
    this.publisherCd = publisherCd;
    this.recommenedAge = recommenedAge;
    this.typeIE = typeIE;
    this.typeSN = typeSN;
    this.typeTF = typeTF;
    this.typePJ = typePJ;
    this.createdAt = createdAt;
    this.updateAt = updateAt;
    this.updateId = updateId;
    this.createId = createId;
    this.displayYn = displayYn;
  }
}

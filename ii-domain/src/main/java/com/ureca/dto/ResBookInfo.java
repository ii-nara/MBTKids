package com.ureca.dto;

import java.util.Date;
import lombok.Data;

// BOK0100-도서상세 화면 응답 DTO
@Data
public class ResBookInfo {

  // 도서 아이디
  private Long bookId;
  // 도서명
  private String bookName;
  // 도서 이미지 URL
  private String bookImgUrl;
  // 줄거리
  private String plot;
  // 작가명
  private String writer;
  // 출판사
  private String publisher;
  // 권장연령
  private String recommenedAge;
  // 성향
  private String mbtiType;
  // 앞 : -1, 뒤 : 1, 상태없음 : 0
  // I/E
  private int typeIE;
  // S/N
  private int typeSN;
  // T/F
  private int typeTF;
  // P/J
  private int typePJ;
  // 등록일자
  private Date createdAt;
  // 등록자
  private Long createId;
  // 수정일자
  private Date updateAt;
  // 수정자
  private Long updateId;
  // 노출여부
  private String displayYn;
  // 좋아요 개수
  private int likeCnt;
  // 싫어요 개수
  private int disLikeCnt;
  // 사용자 : 좋아요 상태
  private String likeStatus;

  // 생성자
  public ResBookInfo(
      Long bookId,
      String bookName,
      String bookImgUrl,
      String plot,
      String writer,
      String publisher,
      String recommenedAge,
      String mbtiType,
      int typeIE,
      int typeSN,
      int typeTF,
      int typePJ,
      Date createdAt,
      Long createId,
      Date updateAt,
      Long updateId,
      String displayYn,
      int likeCnt,
      int disLikeCnt) {
    this.bookId = bookId;
    this.bookName = bookName;
    this.bookImgUrl = bookImgUrl;
    this.plot = plot;
    this.writer = writer;
    this.publisher = publisher;
    this.recommenedAge = recommenedAge;
    this.mbtiType = mbtiType;
    this.typeIE = typeIE;
    this.typeSN = typeSN;
    this.typeTF = typeTF;
    this.typePJ = typePJ;
    this.createdAt = createdAt;
    this.createId = createId;
    this.updateAt = updateAt;
    this.updateId = updateId;
    this.displayYn = displayYn;
    this.likeCnt = likeCnt;
    this.disLikeCnt = disLikeCnt;
  }

  public void updateLikeStatus(String likeStatus) {
    this.likeStatus = likeStatus;
  }
}

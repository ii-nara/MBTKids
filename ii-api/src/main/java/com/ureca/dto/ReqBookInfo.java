package com.ureca.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

// WBK0111 도서 수정 화면 입력 DTO
@Data
public class ReqBookInfo {
  //도서 아이디
  private Long bookId;
  //도서명
  private String bookName;
  // 도서 이미지 파일
  private MultipartFile bookImgUrl;
  // 줄거리
  private String plot;
  //작가명
  private String writer;
  // 작가 코드
  private String writerCd;
  //출판사
  private String publisher;
  // 출판사 코드
  private String publisherCd;
  // 권장연령
  private String recommenedAge;

  //성향
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
  // 노출여부
  private String displayYn;

  // 생성자
  public ReqBookInfo(
      Long bookId,
      String bookName,
      MultipartFile bookImgUrl,
      String plot,
      String writer,
      String writerCd,
      String publisher,
      String publisherCd,
      String recommenedAge,
      String mbtiType,
      int typeIE,
      int typeSN,
      int typeTF,
      int typePJ,
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
    this.mbtiType = mbtiType;
    this.typeIE = typeIE;
    this.typeSN = typeSN;
    this.typeTF = typeTF;
    this.typePJ = typePJ;
    this.displayYn = displayYn;
  }
}

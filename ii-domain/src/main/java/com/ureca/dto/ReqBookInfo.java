package com.ureca.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

// WBK0111-도서수정, WBK0200-도서등록 화면 요청 DTO
@Data
public class ReqBookInfo {
  // 도서 아이디 - 수정만 존재
  private Long bookId;
  // 도서명
  private String bookName;
  // 도서 이미지 URL
  private String bookImgUrl;
  // 도서 이미지 파일
  private MultipartFile bookImgFile;
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
  // 노출여부
  private String displayYn;

  // 생성자
  public ReqBookInfo(
      Long bookId,
      String bookName,
      String bookImgUrl,
      MultipartFile bookImgFile,
      String plot,
      String writer,
      String publisher,
      String recommenedAge,
      String mbtiType,
      int typeIE,
      int typeSN,
      int typeTF,
      int typePJ,
      String displayYn) {
    this.bookId = bookId;
    this.bookImgUrl = bookImgUrl;
    this.bookName = bookName;
    this.bookImgFile = bookImgFile;
    this.plot = plot;
    this.writer = writer;
    this.publisher = publisher;
    this.recommenedAge = recommenedAge;
    this.mbtiType = mbtiType;
    this.typeIE = typeIE;
    this.typeSN = typeSN;
    this.typeTF = typeTF;
    this.typePJ = typePJ;
    this.displayYn = displayYn;
  }
}

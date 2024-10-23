package com.ureca.dto;

import lombok.Data;

@Data
public class BookInfo {

  //도서 아이디
  private Long bookId;
  //도서명
  private String bookName;
  //영화 포스터 URL
  private String bookImgUrl;
  //작가명
  private String writer;
  //출판사
  private String publisher;

  // 생성자
  public BookInfo(
      Long bookId,
      String bookName,
      String bookImgUrl,
      String writer,
      String publisher) {
    this.bookId = bookId;
    this.bookName = bookName;
    this.bookImgUrl = bookImgUrl;
    this.writer = writer;
    this.publisher = publisher;
  }

}

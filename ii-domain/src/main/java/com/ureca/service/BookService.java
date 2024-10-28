package com.ureca.service;

import com.ureca.dto.BookInfo;
import com.ureca.dto.ResBookDetail;
import com.ureca.dto.ResBookInfo;
import com.ureca.entity.BookEntity;
import com.ureca.repository.BookRepository;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  private static final Logger logger = LoggerFactory.getLogger(BookService.class);

  private BookRepository bookRepository;

  public BookService(BookRepository bookRepository) {
    this.bookRepository = bookRepository;
  }

  // 도서 목록 조회
  public List<BookInfo> getBookList(String searchWord) {
    List<BookInfo> bookList = bookRepository.findByBookNameOrWriterOrPublisher(searchWord);
    //logger.info("조회 결과 : "+bookList);

    // 조회된 도서 수와 결과를 로그에 출력
    logger.info("조회된 도서 수: " + bookList.size());
    if (bookList.isEmpty()) {
      logger.warn("조회된 도서가 없습니다.");
    } else {
      logger.info("조회 결과: " + bookList);
    }


    return bookList;
  } //getBookList

  // 홈 - 도서 상세 조회
  public ResBookInfo getBookInfo(Long bookId) {

    BookEntity getBook =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new RuntimeException("data not found"));
    //logger.info("조회 결과 : "+getBook);

    ResBookInfo resBookInfo = convertToResBookInfo(getBook);
    resBookInfo.setLikeCnt(bookRepository.countLikesByBookId(bookId));
    resBookInfo.setDisLikeCnt(bookRepository.countDislikesByBookId(bookId));

    return resBookInfo;
  } //getBookInfo

  // 관리자웹 - 도서 상세 조회
  public ResBookDetail getBookDetail(Long bookId) {

    BookEntity getBook =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new RuntimeException("data not found"));
    //logger.info("조회 결과 : "+getBook);

    ResBookDetail resBookDetail = convertToResBookDetail(getBook);
    resBookDetail.setLikeCnt(bookRepository.countLikesByBookId(bookId));
    resBookDetail.setDisLikeCnt(bookRepository.countDislikesByBookId(bookId));

    return resBookDetail;
  } //getBookInfo

  // BookEntity → ResBookDetail 변환
  private ResBookDetail convertToResBookDetail(BookEntity bookEntity) {
    return new ResBookDetail(
        true,
        bookEntity.getBookId(),
        bookEntity.getBookName(),
        bookEntity.getBookImgUrl(),
        bookEntity.getPlot(),
        bookEntity.getWriter(),
        bookEntity.getPublisher(),
        bookEntity.getRecommenedAge(),
        null, // mbtiType은 null 또는 기본값으로 설정
        bookEntity.getTypeIE(),
        bookEntity.getTypeSN(),
        bookEntity.getTypeTF(),
        bookEntity.getTypePJ(),
        bookEntity.getCreatedAt(),
        bookEntity.getCreateId(), // 등록자
        bookEntity.getUpdateAt(),
        bookEntity.getUpdateId(), // 수정자
        bookEntity.getDisplayYn(),
        0, // likeCnt 기본값 (필요에 따라 조정)
        0  // disLikeCnt 기본값 (필요에 따라 조정)
    );
  } //convertToResBookDetail

  // BookEntity → ResBookInfo 변환
  private ResBookInfo convertToResBookInfo(BookEntity bookEntity) {
    return new ResBookInfo(
        bookEntity.getBookId(),
        bookEntity.getBookName(),
        bookEntity.getBookImgUrl(),
        bookEntity.getPlot(),
        bookEntity.getWriter(),
        bookEntity.getPublisher(),
        bookEntity.getRecommenedAge(),
        null, // mbtiType은 null 또는 기본값으로 설정
        bookEntity.getTypeIE(),
        bookEntity.getTypeSN(),
        bookEntity.getTypeTF(),
        bookEntity.getTypePJ(),
        bookEntity.getCreatedAt(),
        bookEntity.getCreateId(), // 등록자
        bookEntity.getUpdateAt(),
        bookEntity.getUpdateId(), // 수정자
        bookEntity.getDisplayYn(),
        0, // likeCnt 기본값 (필요에 따라 조정)
        0  // disLikeCnt 기본값 (필요에 따라 조정)
    );
  } //convertToResBookInfo

  // 도서 삭제
  public int deleteBookInfo(Long bookId) {
    int result = 0;

    // 존재하는지 확인하고 삭제
    if (bookRepository.existsById(bookId)) {
      bookRepository.deleteById(bookId);
      result = 1;
    } else {
      throw new IllegalArgumentException("Book not found with id: " + bookId);
    }

    return result;
  } //deleteBookInfo

}

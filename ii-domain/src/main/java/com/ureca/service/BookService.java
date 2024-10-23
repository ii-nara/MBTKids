package com.ureca.service;

import com.ureca.dto.BookInfo;
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

  public List<BookInfo> getBookList(String searchWord) {

    // 도서 목록 조회
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
  }

  public ResBookInfo getBookInfo(Long bookId) {

    // 도서 목록 조회
    BookEntity getBook =
        bookRepository
            .findById(bookId)
            .orElseThrow(() -> new RuntimeException("data not found"));
    //logger.info("조회 결과 : "+getBook);

    ResBookInfo resBookInfo = convertToResBookInfo(getBook);
    resBookInfo.setLikeCnt(bookRepository.countLikesByBookId(bookId));
    resBookInfo.setDisLikeCnt(bookRepository.countDislikesByBookId(bookId));

    return resBookInfo;
  }

  // BookEntity → ResBookInfo 변환
  private ResBookInfo convertToResBookInfo(BookEntity bookEntity) {
    return new ResBookInfo(
        bookEntity.getBookId(),
        bookEntity.getBookName(),
        bookEntity.getBookImgUrl(),
        bookEntity.getPlot(),
        bookEntity.getWriter(),
        bookEntity.getWriterCd(),
        bookEntity.getPublisher(),
        bookEntity.getPublisherCd(),
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
  }

}

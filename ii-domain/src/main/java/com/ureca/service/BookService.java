package com.ureca.service;

import com.ureca.dto.BookInfo;
import com.ureca.entity.BookEntity;
import com.ureca.repository.BookRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
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
    List<BookEntity> bookList = bookRepository.findByBookNameOrWriterOrPublisher(searchWord);
    //logger.info("조회 결과 : "+bookList);

    // 조회된 도서 수와 결과를 로그에 출력
    logger.info("조회된 도서 수: " + bookList.size());
    if (bookList.isEmpty()) {
      logger.warn("조회된 도서가 없습니다.");
    } else {
      logger.info("조회 결과: " + bookList);
    }

    // BookEntity를 BookInfo로 변환
    List<BookInfo> resBookList = bookList.stream()
        .map(book -> new BookInfo(
            book.getBookId(),
            book.getBookName(),
            book.getImgURL(),
            book.getWriter(),
            book.getWriterCd(),
            book.getPublisher(),
            book.getPublisherCd()))
        .collect(Collectors.toList());


    return resBookList;
  }

}

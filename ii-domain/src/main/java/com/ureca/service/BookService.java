package com.ureca.service;

import com.ureca.dto.BookInfo;
import com.ureca.dto.ReqBookInfo;
import com.ureca.dto.ResBookDetail;
import com.ureca.dto.ResBookInfo;
import com.ureca.entity.BookEntity;
import com.ureca.repository.BookRepository;
import java.util.Date;
import java.util.List;
import java.util.Optional;
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

  /**
   * @title 도서 목록 조회
   * @description 검색어에 해당하는 도서 조회 목록을 조회한다.
   * @param searchWord 검색어
   * @return List<BookInfo> 도서 목록
   */
  // TODO 관리자/홈 분리하기
  public List<BookInfo> getBookList(String searchWord) {
    List<BookInfo> bookList = bookRepository.findByBookNameOrWriterOrPublisher(searchWord);

    StringBuilder logMessage = new StringBuilder();
    logMessage.append("조회된 도서 수: ").append(bookList.size()).append("\n");
    if (bookList.isEmpty()) {
      logger.warn("조회된 도서가 없습니다.");
    } else {
      logMessage.append("조회 결과: ").append(bookList);
      logger.info(logMessage.toString());
    }

    return bookList;
  } // getBookList

  /**
   * @title 홈 - 도서 상세 조회
   * @description 도서 상세 정보를 조회한다.
   * @param bookId 도서 아이디
   * @return ResBookInfo 도서 정보
   */
  public ResBookInfo getBookInfo(Long bookId) {

    BookEntity getBook =
        bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("data not found"));
    ResBookInfo resBookInfo = convertToResBookInfo(getBook);
    resBookInfo.setLikeCnt(bookRepository.countLikesByBookId(bookId));
    resBookInfo.setDisLikeCnt(bookRepository.countDislikesByBookId(bookId));

    return resBookInfo;
  } // getBookInfo

  /**
   * @title 관리자웹 - 도서 상세 조회
   * @description 도서 상세 정보를 조회한다.
   * @param bookId 도서 아이디
   * @return ResBookDetail 도서 정보
   */
  public ResBookDetail getBookDetail(Long bookId) {

    BookEntity getBook =
        bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("data not found"));
    ResBookDetail resBookDetail = convertToResBookDetail(getBook);
    resBookDetail.setLikeCnt(bookRepository.countLikesByBookId(bookId));
    resBookDetail.setDisLikeCnt(bookRepository.countDislikesByBookId(bookId));

    return resBookDetail;
  } // getBookDetail

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
        0 // disLikeCnt 기본값 (필요에 따라 조정)
        );
  } // convertToResBookDetail

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
        0 // disLikeCnt 기본값 (필요에 따라 조정)
        );
  } // convertToResBookInfo

  /**
   * @title 관리자웹 - 도서 삭제
   * @description 도서 정보를 삭제한다.
   * @param bookId 도서 아이디
   * @return int 삭제 성공 여부
   */
  public int deleteBookInfo(Long bookId) {
    int result = 0;

    if (bookRepository.existsById(bookId)) { // 존재 확인
      bookRepository.deleteById(bookId);
      result = 1;
    } else {
      throw new IllegalArgumentException("Book not found with id: " + bookId);
    }

    return result;
  } // deleteBookInfo

  /**
   * @title 관리자웹 - 도서 등록
   * @description 도서 정보를 신규 등록한다.
   * @param ReqBookInfo 입력한 도서 정보
   * @param uploadUrl 도서 이미지 URL
   * @return Long 생성된 도서 아이디
   */
  public Long saveBookInfo(ReqBookInfo reqBookInfo, String uploadUrl) {
    Long userId = 1L; // TODO 관리자 로그인 정보 가져오기
    Long generatedId = 0L;

    // TODO file -> S3 -> Url

    // 도서명 필수
    if (reqBookInfo.getBookName() != null && reqBookInfo.getBookName().length() < 100) {
      // 데이터 추가
      BookEntity newBook =
          BookEntity.builder()
              .bookName(reqBookInfo.getBookName())
              .bookImgUrl(uploadUrl)
              .plot(reqBookInfo.getPlot())
              .writer(reqBookInfo.getWriter())
              .publisher(reqBookInfo.getPublisher())
              .recommenedAge(reqBookInfo.getRecommenedAge())
              .typeIE(reqBookInfo.getTypeIE())
              .typeSN(reqBookInfo.getTypeSN())
              .typeTF(reqBookInfo.getTypeTF())
              .typePJ(reqBookInfo.getTypePJ())
              .createdAt(new Date())
              .createId(userId)
              .displayYn(reqBookInfo.getDisplayYn())
              .build();
      BookEntity savedBook = bookRepository.save(newBook);
      generatedId = savedBook.getBookId();
    } else {
      logger.info("Book name required, ≤ 100 chars : ", reqBookInfo.getBookName());
    }

    return generatedId;
  } // saveBookInfo

  /**
   * @title 관리자웹 - 도서 수정
   * @description 도서 정보를 업데이트한다.
   * @param ReqBookInfo 수정한 도서 정보
   * @param uploadUrl 도서 이미지 URL
   * @return Long 수정한 도서 아이디
   */
  public Long updateBookInfo(ReqBookInfo reqBookInfo, String uploadUrl) {
    Long userId = 1L; // TODO 관리자 로그인 정보 가져오기
    Long bookId = reqBookInfo.getBookId();

    // 존재하는 도서 정보인지 확인
    Optional<BookEntity> optionalBook = bookRepository.findById(bookId);
    if (optionalBook.isPresent()) {
      // 도서명 필수
      if (reqBookInfo.getBookName() != null && reqBookInfo.getBookName().length() < 100) {
        // 업데이트 내용 구성
        BookEntity updateBook =
            BookEntity.builder()
                .bookId(bookId)
                .bookName(reqBookInfo.getBookName())
                .bookImgUrl(uploadUrl)
                .plot(reqBookInfo.getPlot())
                .writer(reqBookInfo.getWriter())
                .publisher(reqBookInfo.getPublisher())
                .recommenedAge(reqBookInfo.getRecommenedAge())
                .typeIE(reqBookInfo.getTypeIE())
                .typeSN(reqBookInfo.getTypeSN())
                .typeTF(reqBookInfo.getTypeTF())
                .typePJ(reqBookInfo.getTypePJ())
                .createdAt(new Date())
                .createId(userId)
                .displayYn(reqBookInfo.getDisplayYn())
                .build();

        // 변경된 엔티티 업데이트
        bookRepository.save(updateBook);
      } else {
        logger.info("Book name required, ≤ 100 chars : ", reqBookInfo.getBookName());
      }
    } else {
      // 해당 ID에 대한 엔티티가 존재하지 않을 경우의 처리
      System.out.println("Book not found with ID: " + bookId);
    }

    return bookId;
  } // updateBookInfo
}

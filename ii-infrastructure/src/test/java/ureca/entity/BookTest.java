package ureca.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.ureca.entity.*;
import com.ureca.repository.*;
import java.sql.Timestamp;
import java.util.Date;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BookTest {
  private static final Logger logger = LoggerFactory.getLogger(BookTest.class);


  @Autowired
  BookRepository bookRepository;

  // 등록 테스트
  @Test
  void testCreateBook() {
    BookEntity newBook = BookEntity.builder()
        .bookName("테스트 도서 3")
        .imgURL("http://file.koreafilm.or.kr/thm/02/99/18/56/tn_DPF030009.jpg")
        .plot("아이들이 보면 안 되는 범죄 이야기")
        .writer("홍길동")
        .writerCd("W03")
        .publisher("테스트출판사")
        .publisherCd("P03")
        .recommenedAge("12세 이상")
        .typeIE(1)
        .typeSN(1)
        .typeTF(-1)
        .typePJ(-1)
        .createdAt(new Date())
        .displayYn("N")
        .build();

    BookEntity savedBook = bookRepository.save(newBook);
    logger.info("데이터 등록 확인: {}", savedBook);
    assertThat(savedBook).isNotNull();
    assertThat(savedBook.getBookId()).isGreaterThan(0);
  }

  // 조회 테스트
  @Test
  void testReadBook() {
    BookEntity getBook =
        bookRepository
            .findById(1)
            .orElseThrow(() -> new RuntimeException("data not found"));
    logger.info("데이터 조회 확인: { " + getBook + " }");
  }

  // 삭제 테스트
  @Test
  void testDeleteBook() {
    int bookIdToDelete = 3;

    bookRepository.deleteById(bookIdToDelete);
    logger.info("삭제 ID: " + bookIdToDelete);

    boolean exists = bookRepository.existsById(bookIdToDelete);
    logger.info("데이터 삭제 확인 " + bookIdToDelete + " exists: " + exists);
  }

}

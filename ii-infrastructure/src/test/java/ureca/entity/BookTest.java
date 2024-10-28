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
        .bookName("당당 마녀의 중학교 공략집")
        .bookImgUrl("http://file.koreafilm.or.kr/thm/02/99/18/52/tn_DPF029868.jpg")
        .plot("초등학생에서 중학생이 된다는 것은 단순히 학년만 올라가는 것이 아니라 친구, 학교생활 등 주변 환경도 크게 변하게 됨을 의미합니다. 그래서 중학생이 되는 것에 벌써부터 겁을 먹고 있는 친구들도 있을 겁니다. 이 책은 ‘정말 배치 고사를 잘 봐야 학교생활이 편해져?’, ‘친구를 한 명도 못 사귀면 어떡하지?’, ‘정말 일진한테 찍히면 끝장이야?’ 등 중학교를 앞둔 친구들이 궁금해 할만한 질문들을 주제별로 선정하여 궁금증을 풀어주고 있습니다. 초등학교 6학년인 서현이와 윤재가 ‘당당마녀의 중학교 공략집’이란 누리집을 운영하는 ‘당당마녀’에게 문답하는 형식입니다. <br>저자는 실제 초등학교 교사입니다. 그래서인지 내용이 현실적이면서 당연하게만 여겼던 문제들을 다시 한번 생각해 볼 수 있도록 우리에게 질문을 던집니다. 각 장의 끝에는 ‘당당마녀의 생각’ 코너를 실어 생각해 볼 문제나 학교생활 팁을 안내하기도 합니다. 중학교 생활이 궁금한 초등학생들에게 도움이 되는 안내서가 되길 바랍니다.")
        .writer("이기규")
        .writerCd("W05")
        .publisher("책읽는곰")
        .publisherCd("P05")
        .recommenedAge("7세 이상")
        .typeIE(0)
        .typeSN(0)
        .typeTF(0)
        .typePJ(0)
        .createdAt(new Date())
        .displayYn("Y")
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
            .findById(1L)
            .orElseThrow(() -> new RuntimeException("data not found"));
    logger.info("데이터 조회 확인: { " + getBook + " }");
  }

  // 삭제 테스트
  @Test
  void testDeleteBook() {
    int bookIdToDelete = 3;

    bookRepository.deleteById((long) bookIdToDelete);
    logger.info("삭제 ID: " + bookIdToDelete);

    boolean exists = bookRepository.existsById((long) bookIdToDelete);
    logger.info("데이터 삭제 확인 " + bookIdToDelete + " exists: " + exists);
  }

}

package com.ureca;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.ureca.dto.RequestFeedbackDto;
import com.ureca.entity.BookEntity;
import com.ureca.entity.ChildEntity;
import com.ureca.entity.MbtiHistoryEntity;
import com.ureca.entity.MbtiStatusEntity;
import com.ureca.entity.ParentEntity;
import com.ureca.repository.BookRepository;
import com.ureca.repository.ChildRepository;
import com.ureca.repository.MbtiHistoryRepository;
import com.ureca.repository.MbtiStatusRepository;
import com.ureca.repository.ParentRepository;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class FeedBackConcurrencyTest {

  @Autowired
  private RabbitTemplate rabbitTemplate;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private ParentRepository parentJpaRepository;

  @Autowired
  private ChildRepository childRepository;

  @Autowired
  private MbtiStatusRepository mbtiStatusRepository;

  @Autowired
  private MbtiHistoryRepository mbtiHistoryRepository;

  private BookEntity testBook;

  private ParentEntity testParent;

  private List<ChildEntity> testChildren;

  private List<RequestFeedbackDto> testRequestFeedback;

  private long historyCount;

  @BeforeAll
  void setUp() {
    int testCount = 7500;

    testChildren = new ArrayList<>();
    testRequestFeedback = new ArrayList<>();

    testBook =
        bookRepository.save(
            BookEntity.builder()
                .bookName("테스트 도서")
                .typeIE(1)
                .typeSN(1)
                .typeTF(-1)
                .typePJ(-1)
                .build());

    testParent =
        parentJpaRepository.save(
            ParentEntity.builder()
                .email("")
                .parentLoginId("")
                .password("")
                .createdAt(LocalDateTime.now())
                .build());

    for (int i = 0; i < testCount; i++) {
      ChildEntity testChild =
          childRepository.save(
              ChildEntity.builder()
                  .parentId(testParent.getParentId())
                  .childName("테스트 자녀" + i)
                  .childAge(5)
                  .build());

      MbtiHistoryEntity mbtiHistory =
          mbtiHistoryRepository.save(
              MbtiHistoryEntity.builder().typeIE(5).typeSN(5).typeTF(5).typePJ(5).build());

      MbtiStatusEntity mbtiStatus =
          mbtiStatusRepository.save(
              MbtiStatusEntity.builder()
                  .typeIE(5)
                  .typeSN(5)
                  .typeTF(5)
                  .typePJ(5)
                  .childEntity(testChild)
                  .build());

      mbtiStatus.addHistory(mbtiHistory);

      testChild.setMbtiStatusEntity(mbtiStatus);

      testChildren.add(testChild);
    }

    for (int i = 0; i < testCount; i++) {
      testRequestFeedback.add(
          RequestFeedbackDto.of(testChildren.get(i).getChildId(), testBook.getBookId(), 1));
    }

    historyCount = mbtiHistoryRepository.count();

    System.out.println("======================== Set Up is end =====================");
  }

  @Test
  void addFeedback() throws InterruptedException {

    int numThreads = 5000;

    CountDownLatch countDownLatch = new CountDownLatch(numThreads);
    ExecutorService executorService = Executors.newCachedThreadPool(); // 동적

    for (int i = 0; i < numThreads; i++) {
      int finalI = i;

      executorService.execute(
          () -> {
            try {
              rabbitTemplate.convertAndSend(
                  "feedbackExchange", "feedbackRoutingKey", testRequestFeedback.get(finalI));
            } catch (Exception e) {
              e.printStackTrace();
            } finally {
              countDownLatch.countDown(); // 예외 발생해도 countDown 실행
            }
          });
    }

    countDownLatch.await(5, TimeUnit.SECONDS);
    executorService.shutdown();

    Thread.sleep(10000);

    long afterTest = mbtiHistoryRepository.count() - historyCount;

    System.out.println("좋아요 개수 : " + afterTest);
    assertThat(afterTest, is((long) numThreads));
  }
}

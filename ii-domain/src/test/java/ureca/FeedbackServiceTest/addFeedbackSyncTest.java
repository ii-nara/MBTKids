package ureca.FeedbackServiceTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import com.ureca.dto.RequestFeedbackDto;
import com.ureca.entity.BookEntity;
import com.ureca.entity.ChildEntity;
import com.ureca.entity.Enum.LikeStatus;
import com.ureca.entity.MbtiHistoryEntity;
import com.ureca.entity.MbtiStatusEntity;
import com.ureca.entity.ParentEntity;
import com.ureca.repository.BookRepository;
import com.ureca.repository.ChildRepository;
import com.ureca.repository.FeedbackStatusRepository;
import com.ureca.repository.MbtiHistoryRepository;
import com.ureca.repository.MbtiStatusRepository;
import com.ureca.repository.ParentJpaRepository;
import com.ureca.service.FeedbackComponentService;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class addFeedbackSyncTest {

  @Autowired
  private FeedbackComponentService feedbackComponentService;

  @Autowired
  private BookRepository bookRepository;

  @Autowired
  private ParentJpaRepository parentJpaRepository;

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
  @Autowired
  private FeedbackStatusRepository feedbackStatusRepository;


  @BeforeAll
  void setUp() {
    int testCount = 4500;

    testChildren = new ArrayList<>();
    testRequestFeedback = new ArrayList<>();

    testBook = bookRepository.save(BookEntity.builder()
        .bookName("테스트 도서").typeIE(1).typeSN(1).typeTF(-1).typePJ(-1).build());

    testParent = parentJpaRepository.save(ParentEntity.builder().email("").parentLoginId("")
        .password("").createdAt(LocalDateTime.now()).build());

    for (int i = 0; i < testCount; i++) {
      ChildEntity testChild = childRepository.save(ChildEntity.builder()
          .parentId(testParent.getParentId()).childName("테스트 자녀" + i)
          .childAge(5).build());

      MbtiHistoryEntity mbtiHistory = mbtiHistoryRepository.save(MbtiHistoryEntity.builder()
          .typeIE(5).typeSN(5).typeTF(5).typePJ(5).build());

      MbtiStatusEntity mbtiStatus = mbtiStatusRepository.save(MbtiStatusEntity.builder()
          .typeIE(5).typeSN(5).typeTF(5).typePJ(5).childEntity(testChild).build());

      mbtiStatus.addHistory(mbtiHistory);

      testChild.setMbtiStatusEntity(mbtiStatus);

      testChildren.add(testChild);

    }

    for (int i = 0; i < testCount; i++) {
      testRequestFeedback.add(RequestFeedbackDto.of(
          testChildren.get(i).getChildId(), testBook.getBookId(), 1)
      );
    }

    System.out.println("======================== Set Up is end =====================");

  }

  @Test
  void addFeedback() throws InterruptedException {

    int numThreads = 3000;

    CountDownLatch countDownLatch = new CountDownLatch(numThreads);
//    ExecutorService executorService = Executors.newFixedThreadPool(numThreads); // 정적
    ExecutorService executorService = Executors.newCachedThreadPool(); // 동적

    for (int i = 0; i < numThreads; i++) {
      int finalI = i;
      executorService.execute(() -> {
        try {
          feedbackComponentService.addFeedback(testRequestFeedback.get(finalI));
//          if (finalI % 2 == 0) feedbackComponentService.addFeedback(testRequestFeedback.get(finalI)); // 좋아요 취소
        } catch (Exception e) {
          e.printStackTrace();
          System.out.println("충돌 발생");
        } finally {
          countDownLatch.countDown(); // 예외 발생해도 countDown 실행
        }
      });
    }

    countDownLatch.await(10, TimeUnit.SECONDS);
    executorService.shutdown();

//    long afterTest = feedbackStatusRepository.countByBookEntity_BookIdAndLikeStatus(testBook.getBookId(), LikeStatus.LIKE);
//    System.out.println("좋아요 개수 : " + afterTest);
//    assertThat(afterTest, is((long) numThreads));
  }

}

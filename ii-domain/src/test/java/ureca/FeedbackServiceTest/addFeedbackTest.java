package ureca.FeedbackServiceTest;

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
import com.ureca.repository.ParentJpaRepository;
import com.ureca.service.FeedbackComponentService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class addFeedbackTest {

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



  @BeforeAll
  void setUp() {
    testChildren = new ArrayList<>();
    testRequestFeedback = new ArrayList<>();

    testBook = bookRepository.save(BookEntity.builder()
        .bookName("테스트 도서").typeIE(1).typeSN(1).typeTF(-1).typePJ(-1).build());

    testParent = parentJpaRepository.save(ParentEntity.builder().email("").parentLoginId("")
        .password("").createdAt(LocalDateTime.now()).build());

    for (int i = 0; i < 5; i++) {
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

    for (int i = 0; i < 5; i++) {
      testRequestFeedback.add(RequestFeedbackDto.of(
          testChildren.get(i).getChildId(), testBook.getBookId(), 1)
      );
    }

    historyCount = mbtiHistoryRepository.count();

  }

  @Test
  void addFeedback() {
    for (int childNum = 0; childNum < 5; childNum++) {
      for (int feedbackCount = 0; feedbackCount < 100; feedbackCount++) {
        feedbackComponentService.addFeedback(testRequestFeedback.get(childNum));
      }
    }

    long afterTest = mbtiHistoryRepository.count();
    assertThat(historyCount, is(afterTest - 500));
  }
}

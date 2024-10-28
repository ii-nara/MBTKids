package ureca.FeedbackServiceTest;

import com.ureca.dto.ResponseFeedbackDto;
import com.ureca.entity.ChildEntity;
import com.ureca.entity.Enum.LikeStatus;
import com.ureca.entity.MbtiStatusEntity;
import com.ureca.service.MbtiManagementService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class updateMbtiStatusTest {

  @Autowired
  private MbtiManagementService mbtiManagementService;

  private MbtiStatusEntity mbtiStatus;

  private ChildEntity child;

  private ResponseFeedbackDto responseFeedbackDto;

  @BeforeAll
  void setUp() {
    mbtiStatus = MbtiStatusEntity.builder()
        .typeIE(5)
        .typeSN(5)
        .typeTF(5)
        .typePJ(5).build();

    child = ChildEntity.builder()
        .parentId(1L)
        .mbtiStatusEntity(mbtiStatus)
        .childName("테스트")
        .childAge(5)
        .createdAt(LocalDateTime.now()).build();

    responseFeedbackDto = responseFeedbackDto.of(1L, 1, 1, 1, -1, -1, LikeStatus.DISLIKE);

    System.out.println("============ 최초 상태 ==================");
    System.out.println("now MBTI"
        + "\nIE -> " + mbtiStatus.getTypeIE()
        + "\nSN -> " + mbtiStatus.getTypeSN()
        + "\nTF -> " + mbtiStatus.getTypeTF()
        + "\npj -> " + mbtiStatus.getTypePJ()
    );
    System.out.println("==============================");
  }

  @AfterEach
  void printFeedback() {
    System.out.println("now MBTI"
        + "\nIE -> " + mbtiStatus.getTypeIE()
        + "\nSN -> " + mbtiStatus.getTypeSN()
        + "\nTF -> " + mbtiStatus.getTypeTF()
        + "\npj -> " + mbtiStatus.getTypePJ()
    );
    System.out.println("==============================");
  }

  @AfterAll
  void printResult() {
    System.out.println("History Count : " + mbtiStatus.getMbtiHistoryEntities().size());
  }

  @Test
  @Order(5)
  void test1() {
    mbtiManagementService.updateMbtiStatus(child, responseFeedbackDto);
  }

  @Test
  @Order(4)
  void test2() {
    mbtiManagementService.updateMbtiStatus(child, responseFeedbackDto);
  }

  @Test
  @Order(3)
  void test3() {
    mbtiManagementService.updateMbtiStatus(child, responseFeedbackDto);
  }

  @Test
  @Order(2)
  void test4() {
    mbtiManagementService.updateMbtiStatus(child, responseFeedbackDto);
  }

  @Test
  @Order(1)
  void test5() {
    mbtiManagementService.updateMbtiStatus(child, responseFeedbackDto);
  }

  @Test
  @Order(0)
  void test6() {
    mbtiManagementService.updateMbtiStatus(child, responseFeedbackDto);
  }

}

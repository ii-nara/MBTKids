package ureca.FeedbackServiceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.ureca.entity.Enum.LikeStatus;
import com.ureca.entity.FeedbackStatusEntity;
import com.ureca.service.FeedbackManagementService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class updateFeedbackStatusTest {

  @Autowired
  private FeedbackManagementService feedbackManagementService;

  private FeedbackStatusEntity feedbackStatus;

  @BeforeEach
  void setUp() {
    System.out.println("========== 기초 데이터 생성 ==========");
    feedbackStatus = FeedbackStatusEntity.builder().childEntity(null).bookEntity(null).isLike(LikeStatus.CANCELED).build();
    System.out.println("시작 상태 : " + feedbackStatus.getIsLike().name() + "\n================================");
  }

  @AfterEach
  void printNewLine() {
    System.out.println("\n===========================\n");
  }

  @Test
  @DisplayName("좋아요 - 좋아요 - 좋아요 - 싫어요")
  @Order(1)
  public void requestLIKE() {
    int result = feedbackManagementService.updateFeedbackStatus(feedbackStatus, 1);
    assertThat(result, is(1));
    System.out.println("좋아요를 눌렀을 때 : " + result + " 현재 상태 : " + feedbackStatus.getIsLike().name());

    result = feedbackManagementService.updateFeedbackStatus(feedbackStatus, 1);
    assertThat(result, is(-1));
    System.out.println("좋아요를 눌렀을 때 : " + result + " 현재 상태 : " + feedbackStatus.getIsLike().name());

    result = feedbackManagementService.updateFeedbackStatus(feedbackStatus, 1);
    assertThat(result, is(1));
    System.out.println("좋아요를 눌렀을 때 : " + result + " 현재 상태 : " + feedbackStatus.getIsLike().name());

    result = feedbackManagementService.updateFeedbackStatus(feedbackStatus, -1);
    assertThat(result, is(-1));
    System.out.println("싫어요를 눌렀을 때 : " + result + " 현재 상태 : " + feedbackStatus.getIsLike().name());
  }

  @Test
  @DisplayName("싫어요 - 싫어요 - 싫어요 - 좋아요")
  @Order(2)
  public void requestDISLIKE() {
    int result = feedbackManagementService.updateFeedbackStatus(feedbackStatus, -1);
    assertThat(result, is(-1));
    System.out.println("싫어요를 눌렀을 때 : " + result + " 현재 상태 : " + feedbackStatus.getIsLike().name());

    result = feedbackManagementService.updateFeedbackStatus(feedbackStatus, -1);
    assertThat(result, is(1));
    System.out.println("싫어요를 눌렀을 때 : " + result + " 현재 상태 : " + feedbackStatus.getIsLike().name());

    result = feedbackManagementService.updateFeedbackStatus(feedbackStatus, -1);
    assertThat(result, is(-1));
    System.out.println("싫어요를 눌렀을 때 : " + result + " 현재 상태 : " + feedbackStatus.getIsLike().name());

    result = feedbackManagementService.updateFeedbackStatus(feedbackStatus, 1);
    assertThat(result, is(1));
    System.out.println("좋아요를 눌렀을 때 : " + result + " 현재 상태 : " + feedbackStatus.getIsLike().name());
  }


}

package ureca.entity;

import com.ureca.MainApplication;
import com.ureca.entity.Enum.LikeStatus;
import com.ureca.entity.FeedbackStatusEntity;
import com.ureca.repository.FeedbackStatusRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MainApplication.class)
public class FeetbackStatusTest {

  private static final Logger logger = LoggerFactory.getLogger(FeetbackStatusTest.class);

  @Autowired
  FeedbackStatusRepository feedbackStatusRepository;

  @Test
  public void insertFeetback() throws Exception {
    FeedbackStatusEntity newFeetbackLike = FeedbackStatusEntity.builder()
        .childEntity(null)
        .bookEntity(null)
        .isLike(LikeStatus.LIKE) // 1 : 좋아요
        .build();

    FeedbackStatusEntity savedFeedback = feedbackStatusRepository.save(newFeetbackLike);
    logger.info(savedFeedback.toString());

    FeedbackStatusEntity newFeetbackDisLike = FeedbackStatusEntity.builder()
        .childEntity(null)
        .bookEntity(null)
        .isLike(LikeStatus.DISLIKE) // -1 : 싫어요
        .build();

    savedFeedback = feedbackStatusRepository.save(newFeetbackDisLike);
    logger.info(savedFeedback.toString());

    FeedbackStatusEntity newFeetbackCanceled = FeedbackStatusEntity.builder()
        .childEntity(null)
        .bookEntity(null)
        .isLike(LikeStatus.CANCELED) // 0 : 취소(반응 없음)
        .build();

    savedFeedback = feedbackStatusRepository.save(newFeetbackCanceled);
    logger.info(savedFeedback.toString());

    try {
      FeedbackStatusEntity newFeetbackByNum = FeedbackStatusEntity.builder()
          .childEntity(null)
          .bookEntity(null)
          .isLike(LikeStatus.fromValue(2)) // -1 ~ 1 외의 값
          .build();

      savedFeedback = feedbackStatusRepository.save(newFeetbackByNum);
      logger.info(savedFeedback.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }


  }

}

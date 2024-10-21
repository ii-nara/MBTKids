package ureca.entity;

import com.ureca.MainApplication;
import com.ureca.entity.Enum.LikeStatus;
import com.ureca.entity.FeedbackLogEntity;
import com.ureca.repository.FeedbackLogRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = MainApplication.class)
public class FeedbackLogTest {

  private static final Logger logger = LoggerFactory.getLogger(FeedbackLogTest.class);

  @Autowired
  FeedbackLogRepository feedbackLogRepository;

  @Test
  public void insertFeetback() throws Exception {
    FeedbackLogEntity newFeetbackLike = FeedbackLogEntity.builder()
        .childEntity(null)
        .bookEntity(null)
        .isLike(LikeStatus.LIKE) // 1 : 좋아요
        .build();

    FeedbackLogEntity savedFeedback = feedbackLogRepository.save(newFeetbackLike);
    logger.info(savedFeedback.toString());

    FeedbackLogEntity newFeetbackDisLike = FeedbackLogEntity.builder()
        .childEntity(null)
        .bookEntity(null)
        .isLike(LikeStatus.DISLIKE) // -1 : 싫어요
        .build();

    savedFeedback = feedbackLogRepository.save(newFeetbackDisLike);
    logger.info(savedFeedback.toString());

    FeedbackLogEntity newFeetbackCanceled = FeedbackLogEntity.builder()
        .childEntity(null)
        .bookEntity(null)
        .isLike(LikeStatus.CANCELED) // 0 : 취소(반응 없음)
        .build();

    savedFeedback = feedbackLogRepository.save(newFeetbackCanceled);
    logger.info(savedFeedback.toString());

    try {
      FeedbackLogEntity newFeetbackByNum = FeedbackLogEntity.builder()
          .childEntity(null)
          .bookEntity(null)
          .isLike(LikeStatus.fromValue(2)) // -1 ~ 1 외의 값
          .build();

      savedFeedback = feedbackLogRepository.save(newFeetbackByNum);
      logger.info(savedFeedback.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }


  }
}
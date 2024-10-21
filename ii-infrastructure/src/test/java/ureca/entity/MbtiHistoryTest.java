package ureca.entity;

import com.ureca.MainApplication;
import com.ureca.entity.MbtiHistoryEntity;
import com.ureca.repository.MbtiHistoryRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest(classes = MainApplication.class)
public class MbtiHistoryTest {

  private static final Logger logger = LoggerFactory.getLogger(FeetbackStatusTest.class);

  @Autowired
  MbtiHistoryRepository mbtiHistoryRepository;

  @Test
  public void insertMbti() {
    for (int i = 0; i < 30; i++) {
      MbtiHistoryEntity newMbtiStatus = MbtiHistoryEntity.builder()
          .childEntity(null)
          .feedbackLogEntity(null)
          .typeIE(1)
          .typeSN(10)
          .typeTF(1)
          .typePJ(10)
          .build();

      MbtiHistoryEntity savedMbtiStatus = mbtiHistoryRepository.save(newMbtiStatus);
      logger.info(savedMbtiStatus.toString());
    }
  }

}
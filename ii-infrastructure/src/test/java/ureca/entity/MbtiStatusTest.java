package ureca.entity;

import com.ureca.entity.MbtiStatusEntity;
import com.ureca.repository.MbtiStatusRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
public class MbtiStatusTest {

  private static final Logger logger = LoggerFactory.getLogger(MbtiStatusTest.class);

  @Autowired
  MbtiStatusRepository mbtiStatusRepository;

  @Test
  public void insertMbti() {
    for (int i = 0; i < 10; i++) {
      MbtiStatusEntity newMbtiStatus = MbtiStatusEntity.builder()
          .typeIE(1)
          .typeSN(10)
          .typeTF(3)
          .typePJ(8)
          .build();

      MbtiStatusEntity savedMbtiStatus = mbtiStatusRepository.save(newMbtiStatus);
      logger.info(savedMbtiStatus.toString());
    }


  }

}
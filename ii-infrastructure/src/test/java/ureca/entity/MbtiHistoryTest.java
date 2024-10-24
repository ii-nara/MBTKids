package ureca.entity;

import com.ureca.entity.Enum.LikeStatus;
import com.ureca.entity.MbtiHistoryEntity;
import com.ureca.entity.MbtiStatusEntity;
import com.ureca.repository.MbtiHistoryRepository;
import com.ureca.repository.MbtiStatusRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Transactional
@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
public class MbtiHistoryTest {

  private static final Logger logger = LoggerFactory.getLogger(FeetbackStatusTest.class);

  @Autowired
  MbtiHistoryRepository mbtiHistoryRepository;
  @Autowired
  private MbtiStatusRepository mbtiStatusRepository;

  private MbtiStatusEntity mbtiStatusEntity;

  @BeforeAll
  public void setup() {
    mbtiStatusEntity = mbtiStatusRepository.save(MbtiStatusEntity
        .builder().typeIE(1)
        .typeSN(10)
        .typeTF(3)
        .typePJ(8).build());
  }

  @Test
  public void insertMbti() {
    for (int i = 0; i < 30; i++) {
      MbtiHistoryEntity newMbtiStatus = MbtiHistoryEntity.builder()
          .isLike(LikeStatus.valueOf("LIKE"))
          .bookId(1L)
          .typeIE(1)
          .typeSN(10)
          .typeTF(1)
          .typePJ(10)
          .build();

      mbtiStatusEntity.addHistory(newMbtiStatus);
    }
    mbtiStatusRepository.save(mbtiStatusEntity);
  }

  @Test
  void delete() {
    // 부모가 사라지면 같이 사라집니다.
    mbtiStatusRepository.delete(mbtiStatusRepository.findById(mbtiStatusEntity.getMbtiStausId() - 1L).get());
  }

}
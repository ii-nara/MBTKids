package ureca.entity;

import static org.assertj.core.api.Assertions.assertThat;

import com.ureca.entity.RecommendEntity;
import com.ureca.repository.RecommendRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class RecommendEntityTest {

  @Autowired
  private RecommendRepository recommendRepository;

  @Test
  void create() {
    RecommendEntity entity = RecommendEntity.builder()
        .bookId(1L)
        .childId(1L)
        .recommendAt(LocalDateTime.now())
        .deleteYn("N")
        .build();
    RecommendEntity savedEntity = recommendRepository.save(entity);
    Optional<RecommendEntity> findEntity = recommendRepository.findById(entity.getRecommendId());
    assertThat(savedEntity).isEqualTo(findEntity.orElse(null));
  }
}
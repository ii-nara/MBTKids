package com.ureca.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.ureca.MainApplication;
import com.ureca.entity.RecommendEntity;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest(classes = MainApplication.class)
class RecommendRepositoryTest {

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
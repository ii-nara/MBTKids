package com.ureca.repository;

import com.ureca.entity.FeedbackStatusEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface FeedbackStatusRepository extends JpaRepository<FeedbackStatusEntity, Long> {
  Optional<FeedbackStatusEntity> findByBookEntity_BookIdAndChildEntity_ChildId(
      Long bookId, Long childId);

  long countByBookEntity_BookId(Long bookId);

  @Modifying
  @Transactional
  @Query("DELETE FROM FeedbackStatusEntity f WHERE f.bookEntity.bookId = :bookId")
  void deleteByBookId(Long bookId);
}

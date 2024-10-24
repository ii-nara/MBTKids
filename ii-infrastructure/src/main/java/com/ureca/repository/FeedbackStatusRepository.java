package com.ureca.repository;

import com.ureca.entity.FeedbackStatusEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackStatusRepository extends JpaRepository<FeedbackStatusEntity, Long> {
  Optional<FeedbackStatusEntity> findByBookEntity_BookIdAndChildEntity_ChildId(Long bookId, Long childId);
}
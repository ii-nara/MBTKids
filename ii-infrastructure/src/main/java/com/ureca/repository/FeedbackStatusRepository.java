package com.ureca.repository;

import com.ureca.entity.FeedbackStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackStatusRepository extends JpaRepository<FeedbackStatusEntity, Long> {

}

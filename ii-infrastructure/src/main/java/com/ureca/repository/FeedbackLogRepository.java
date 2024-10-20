package com.ureca.repository;

import com.ureca.entity.FeedbackLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackLogRepository extends JpaRepository<FeedbackLogEntity, Long> {

}

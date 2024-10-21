package com.ureca.repository;

import com.ureca.entity.FeedbackLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackLogRepository extends JpaRepository<FeedbackLogEntity, Long> {

}
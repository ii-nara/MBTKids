package com.ureca.repository;

import com.ureca.entity.MbtiStatusEntity;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MbtiStatusRepository extends JpaRepository<MbtiStatusEntity, Long> {

  void deleteByDeleteAtIsBefore(LocalDateTime deleteAt);
}

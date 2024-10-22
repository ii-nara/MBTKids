package com.ureca.repository;

import com.ureca.entity.ChildEntity;
import com.ureca.entity.MbtiStatusEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MbtiStatusRepository extends JpaRepository<MbtiStatusEntity, Long> {

  Optional<MbtiStatusEntity> findByChildEntity(ChildEntity child);
}
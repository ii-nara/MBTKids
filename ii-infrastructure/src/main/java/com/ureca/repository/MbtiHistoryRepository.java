package com.ureca.repository;

import com.ureca.entity.ChildEntity;
import com.ureca.entity.MbtiHistoryEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MbtiHistoryRepository extends JpaRepository<MbtiHistoryEntity, Long> {

  List<MbtiHistoryEntity> findAllByChildEntityOrderByTimeStamp(ChildEntity child);
}
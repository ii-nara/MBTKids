package com.ureca.repository;

import com.ureca.entity.ChildEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<ChildEntity, Long> {

  List<ChildEntity> findByParentId(Long parentId);
}

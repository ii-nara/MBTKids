package com.ureca.repository;

import com.ureca.entity.ParentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentJpaRepository extends JpaRepository<ParentEntity, Long> {

  Optional<ParentEntity> findByParentLoginId(String parentLoginId);
}

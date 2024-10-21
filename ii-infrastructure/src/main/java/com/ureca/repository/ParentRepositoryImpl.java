package com.ureca.repository;

import com.ureca.domain.Parent;
import com.ureca.entity.ParentEntity;
import com.ureca.service.port.ParentRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ParentRepositoryImpl implements ParentRepository {

  private final ParentJpaRepository parentJpaRepository;

  @Override
  public Parent save(Parent parent) {
    return parentJpaRepository.save(ParentEntity.from(parent)).toModel();
  }

  @Override
  public Optional<Parent> findByParentLoginId(String parentLoginId) {
    return parentJpaRepository.findByParentLoginId(parentLoginId)
        .map(ParentEntity::toModel);
  }
}

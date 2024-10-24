package com.ureca.service;

import com.ureca.dto.ChildCreateDto;
import com.ureca.entity.ChildEntity;
import com.ureca.entity.ParentEntity;
import com.ureca.repository.ChildRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChildAddServiceImpl {

  private final ChildRepository childRepository;

  public void addChild(ParentEntity parent, ChildCreateDto dto) {
    ChildEntity child = ChildEntity.builder()
        .childName(dto.getChildName())
        .childAge(dto.getChildAge())
        .parentId(parent.getParentId())
        .createdAt(LocalDateTime.now())
        .build();
    childRepository.save(child);
  }

  public List<ChildEntity> findChildrenByParentId(Long parentId) {
    return childRepository.findByParentId(parentId);
  }

  public ChildEntity findChildById(Long childId) {
    return childRepository.findById(childId)
        .orElseThrow(() -> new IllegalArgumentException("해당 자식이 존재하지 않습니다."));
  }
}

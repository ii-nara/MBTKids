package com.ureca.service.port;

import com.ureca.domain.Parent;
import java.util.Optional;


public interface ParentRepository {

  Parent save(Parent parent);

  Optional<Parent> findByParentLoginId(String parentLoginId);
}

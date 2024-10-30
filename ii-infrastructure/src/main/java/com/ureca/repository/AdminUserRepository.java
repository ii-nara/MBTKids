package com.ureca.repository;

import com.ureca.entity.AdminUserEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUserEntity, Integer> {

  Optional<AdminUserEntity> findByAdminLoginId(String adminLoginId);
}

package com.ureca.repository;

import com.ureca.entity.AdminUserEntity;
import com.ureca.entity.BookEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminUserRepository extends JpaRepository<AdminUserEntity, Integer> {

}

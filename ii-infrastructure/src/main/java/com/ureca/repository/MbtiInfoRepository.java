package com.ureca.repository;

import com.ureca.entity.MbtiInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MbtiInfoRepository extends JpaRepository<MbtiInfoEntity, Integer> {

  MbtiInfoEntity findByMbtiNm(String mbtiNm);
}

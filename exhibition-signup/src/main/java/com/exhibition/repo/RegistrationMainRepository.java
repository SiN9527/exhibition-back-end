package com.exhibition.repo;


import com.exhibition.entity.RegistrationMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RegistrationMainRepository extends JpaRepository<RegistrationMainEntity, String>, JpaSpecificationExecutor<RegistrationMainEntity> {
    Optional<RegistrationMainEntity> findByEventIdAndMemberId(String eventId, String memberId);

    // 根據前綴查找最大 reg_id（例如：DP20250422%）
    @Query("SELECT MAX(r.registrationId) FROM RegistrationMainEntity r WHERE r.registrationId LIKE :prefix%")
    String findMaxRegistrationIdByPrefix(@Param("prefix") String prefix);

    List<RegistrationMainEntity> findByMemberIdAndGroupCode(String memberId, String groupCode);

    List<RegistrationMainEntity> findByGroupCode(String groupCode);

    RegistrationMainEntity findByMemberId(String memberId);
}
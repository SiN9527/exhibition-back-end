package com.exhibition.repo;


import com.exhibition.entity.RegistrationGroupMainEntity;
import com.exhibition.entity.RegistrationMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistrationGroupMainRepository extends JpaRepository<RegistrationGroupMainEntity, String> {
    List<RegistrationGroupMainEntity> findByEventIdOrderByGroupIdAsc(String eventId);

    List<RegistrationMainEntity> findByEventIdAndGroupId(String eventId, String groupId);
}
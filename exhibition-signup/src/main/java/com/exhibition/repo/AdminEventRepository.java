package com.exhibition.repo;


import com.exhibition.entity.AdminEventEntity;
import com.exhibition.entity.AdminEventPkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminEventRepository extends JpaRepository<AdminEventEntity, AdminEventPkEntity> {


    Optional<AdminEventEntity> findByAccountAndEventId(String account, String eventId);
}
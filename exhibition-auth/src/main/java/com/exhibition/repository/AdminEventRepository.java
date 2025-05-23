package com.exhibition.repository;

import com.svc.ems.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminEventRepository extends JpaRepository<AdminEventEntity, AdminEventPkEntity> {


    Optional<AdminEventEntity> findByAccountAndEventId(String account, String eventId);
}
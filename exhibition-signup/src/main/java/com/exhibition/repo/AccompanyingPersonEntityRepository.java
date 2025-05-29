package com.exhibition.repo;


import com.exhibition.entity.AccompanyingPersonEntity;
import com.exhibition.entity.AdminEventPkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccompanyingPersonEntityRepository extends JpaRepository<AccompanyingPersonEntity, AdminEventPkEntity> {
}
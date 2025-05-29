package com.exhibition.repo;


import com.exhibition.entity.RegistrationPromoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistrationPromoRepository extends JpaRepository<RegistrationPromoEntity, Long> {
}
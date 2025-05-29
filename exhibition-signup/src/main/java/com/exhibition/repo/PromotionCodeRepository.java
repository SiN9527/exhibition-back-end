package com.exhibition.repo;


import com.exhibition.entity.PromotionCodeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionCodeRepository extends JpaRepository<PromotionCodeEntity, String> {
}
package com.exhibition.repository;

import com.exhibition.entity.admin.AdminMainEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AdminMainRepository extends JpaRepository<AdminMainEntity, Long> {

    Optional<AdminMainEntity> findByUserName(String username);

    Optional<AdminMainEntity> findByEmail(String email);

    Optional<AdminMainEntity> findByUserNameOrEmail(String username, String email);

    Boolean existsByUserName(String username);

    Boolean existsByEmail(String email);

    Boolean existsByAccount(String account);

    Boolean existsByEmailAndPassword(String email, String password);

    Optional<AdminMainEntity> findByAccount(String account);
}
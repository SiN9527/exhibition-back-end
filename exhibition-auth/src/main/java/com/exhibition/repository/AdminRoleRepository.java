package com.exhibition.repository;


import com.exhibition.entity.admin.AdminRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRoleRepository extends JpaRepository<AdminRoleEntity, Long> {

    
}

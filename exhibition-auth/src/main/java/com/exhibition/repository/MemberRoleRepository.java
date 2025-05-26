package com.exhibition.repository;


import com.exhibition.entity.member.MemberRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRoleRepository  extends JpaRepository<MemberRoleEntity, Integer> {

    
}

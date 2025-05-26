package com.exhibition.repository;


import com.exhibition.entity.member.MemberRoleEventEntity;
import com.exhibition.entity.member.MemberRoleEventPkEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRoleEventRepository extends JpaRepository<MemberRoleEventEntity, MemberRoleEventPkEntity> {
}